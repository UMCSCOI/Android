package com.stable.scoi.domain.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.stable.scoi.data.api.OkHttpUpbitCandleWsApi
import com.stable.scoi.data.api.UpbitQuotationRestApi
import com.stable.scoi.data.dto.response.UpbitMinuteCandleDto.Companion.toTvCandle
import com.stable.scoi.domain.model.CandleStreamEvent
import com.stable.scoi.domain.model.ParsedUpbitWs
import com.stable.scoi.domain.model.RecentTrade
import com.stable.scoi.domain.model.TvCandle
import com.stable.scoi.domain.model.UpbitWsEvent
import com.stable.scoi.util.SLOG
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.collections.asReversed
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class DummyRepository @Inject constructor(
    private val rest: UpbitQuotationRestApi,
    private val wsApi: OkHttpUpbitCandleWsApi,
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun streamMarket(
        market: String,
        unitMinutes: Int,
        initialCount: Int
    ): Flow<CandleStreamEvent> = channelFlow {

        // 1) REST 분봉 200개 스냅샷
        val snapshot = rest.getMinuteCandles(
            unit = unitMinutes,
            market = market,
            count = initialCount.coerceAtMost(200),
        )
            .asReversed() // 오래된 -> 최신
            .map { it.toTvCandle() }

        send(CandleStreamEvent.Snapshot(snapshot))

        // 2) WS 통합 스트림 (candle + trade)
        val wsJob = launch {
            wsApi.streamMinuteCandle(
                markets = listOf(market),
                unitMinutes = unitMinutes,
                subscribeCandle = true,
                subscribeTrade = true
            )
                .catch { e ->
                    SLOG.D("Repo: WS error $e")
                }
                .collect { ev ->
                    when (ev) {
                        is UpbitWsEvent.Message -> {
                            val parsed = parseUpbitWsMessage(ev.rawJson, expectedMarket = market)
                                ?: return@collect

                            when (parsed) {
                                is ParsedUpbitWs.Candle ->
                                    send(CandleStreamEvent.Update(parsed.value))

                                is ParsedUpbitWs.Trade ->
                                    send(CandleStreamEvent.TradeUpdate(parsed.value))
                            }
                        }

                        is UpbitWsEvent.Failure -> {
                            SLOG.D("Repo: WS failure=$ev")
                        }

                        is UpbitWsEvent.Closing -> {
                            SLOG.D("Repo: WS closing=$ev")
                        }

                        else -> Unit
                    }
                }
        }

        awaitClose {
            wsJob.cancel()
            wsApi.close()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val UPBIT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseUpbitWsMessage(
        rawJson: String,
        expectedMarket: String,
    ): ParsedUpbitWs? {
        val obj = runCatching { JSONObject(rawJson) }.getOrNull() ?: return null

        val type = obj.optString("type") // "candle.1m" or "trade"
        val code = obj.optString("code")
        if (code != expectedMarket) return null

        return when {
            type.startsWith("candle.") -> {
                parseCandle(obj)?.let { ParsedUpbitWs.Candle(it) }
            }

            type == "trade" -> {
                parseTrade(obj)?.let { ParsedUpbitWs.Trade(it) }
            }

            else -> null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseCandle(obj: JSONObject): TvCandle? {
        val utc = obj.optString("candle_date_time_utc")
        if (utc.isBlank()) return null

        val timeSec = LocalDateTime.parse(utc, UPBIT_FMT).toEpochSecond(ZoneOffset.UTC)

        val open = obj.optDouble("opening_price", Double.NaN)
        val high = obj.optDouble("high_price", Double.NaN)
        val low = obj.optDouble("low_price", Double.NaN)
        val close = obj.optDouble("trade_price", Double.NaN)
        val volume = obj.optDouble("candle_acc_trade_volume", Double.NaN)

        if (open.isNaN() || high.isNaN() || low.isNaN() || close.isNaN() || volume.isNaN()) return null

        val volumeColor = if (close >= open) "#ff4d4f" else "#2f7cff"

        return TvCandle(
            time = timeSec,
            open = open,
            high = high,
            low = low,
            close = close,
            volume = volume,
            volumeColor = volumeColor
        )
    }

    private fun parseTrade(obj: JSONObject): RecentTrade? {
        val market = obj.optString("code")
        if (market.isBlank()) return null

        // 업비트 trade 메시지엔 trade_timestamp(ms)가 보통 있음
        val tsMs = when {
            obj.has("trade_timestamp") -> obj.optLong("trade_timestamp", 0L)
            obj.has("timestamp") -> obj.optLong("timestamp", 0L)
            else -> 0L
        }
        if (tsMs <= 0L) return null

        val price = obj.optDouble("trade_price", Double.NaN)
        val volume = obj.optDouble("trade_volume", Double.NaN)
        if (price.isNaN() || volume.isNaN()) return null

        val askBid = obj.optString("ask_bid") // "ASK"(매도) / "BID"(매수)
        if (askBid.isBlank()) return null

        // 색상 룰(원하는대로 바꾸면 됨)
        val color = if (askBid == "BID") "#EF2B2A" else "#2569F2"

        return RecentTrade(
            market = market,
            timestampMs = tsMs,
            price = price,
            volume = volume,
            askBid = askBid,
            color = color
        )
    }

    @OptIn(ExperimentalTime::class)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun upbitUtcToEpochSeconds(utc: String): Long {
        // Upbit REST/WS는 보통 "2025-07-29T00:00:00" (UTC) 처럼 옴
        return runCatching {
            Instant.parse(utc).epochSeconds
        }.getOrElse {
            val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            LocalDateTime.parse(utc, fmt).toEpochSecond(ZoneOffset.UTC)
        }
    }
}