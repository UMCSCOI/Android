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
import com.stable.scoi.domain.model.UpbitTicker
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

class ChartRepository @Inject constructor(
    private val rest: UpbitQuotationRestApi,
    private val wsApi: OkHttpUpbitCandleWsApi,
) {

    @RequiresApi(Build.VERSION_CODES.O)
    // ChartRepository.kt

    fun streamUnified(
        chartMarket: String? = null,   // ✅ Nullable로 변경 (Default = null)
        tickerMarkets: List<String>,
        unitMinutes: Int = 1
    ): Flow<CandleStreamEvent> = channelFlow {

        // 1) REST API 스냅샷: chartMarket이 있을 때만 요청
        if (chartMarket != null) {
            try {
                val snapshot = rest.getMinuteCandles(
                    unit = unitMinutes,
                    market = chartMarket,
                    count = 200,
                )
                    .asReversed()
                    .map { it.toTvCandle() }

                send(CandleStreamEvent.Snapshot(snapshot))
            } catch (e: Exception) {
                SLOG.D("Repo: REST Snapshot error $e")
            }
        }

        // 2) WS 통합 연결
        // listOfNotNull: null인 chartMarket은 자동으로 제외됨
        val allMarkets = (listOfNotNull(chartMarket) + tickerMarkets).distinct()

        // chartMarket이 없으면 캔들/체결은 구독할 필요 없음 (데이터 절약)
        val needChart = chartMarket != null

        val wsJob = launch {
            wsApi.streamMinuteCandle(
                markets = allMarkets,
                unitMinutes = unitMinutes,
                subscribeCandle = needChart, // ✅ 동적 설정
                subscribeTrade = needChart,  // ✅ 동적 설정
                subscribeTicker = true       // Ticker는 항상 구독
            )
                .catch { e -> SLOG.D("Repo: WS error $e") }
                .collect { ev ->
                    if (ev is UpbitWsEvent.Message) {
                        val parsed = parseUpbitWsMessage(ev.rawJson) ?: return@collect

                        when (parsed) {
                            // [캔들] chartMarket이 null이 아니고, 마켓이 일치할 때만
                            is ParsedUpbitWs.Candle -> {
                                val code = JSONObject(ev.rawJson).optString("code")
                                if (chartMarket != null && code == chartMarket) {
                                    send(CandleStreamEvent.Update(parsed.value))
                                }
                            }

                            // [체결] chartMarket이 null이 아니고, 마켓이 일치할 때만
                            is ParsedUpbitWs.Trade -> {
                                if (chartMarket != null && parsed.value.market == chartMarket) {
                                    send(CandleStreamEvent.TradeUpdate(parsed.value))
                                }
                            }

                            // [Ticker] 요청한 Ticker 목록에 포함될 때만
                            is ParsedUpbitWs.Ticker -> {
                                if (parsed.value.market in tickerMarkets) {
                                    send(CandleStreamEvent.TickerUpdate(parsed.value))
                                }
                            }
                        }
                    }
                }
        }

        awaitClose {
            wsJob.cancel()
            wsApi.close()
        }
    }

    // --- Parsing Logic ---

    @RequiresApi(Build.VERSION_CODES.O)
    private val UPBIT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseUpbitWsMessage(rawJson: String): ParsedUpbitWs? {
        val obj = runCatching { JSONObject(rawJson) }.getOrNull() ?: return null
        val type = obj.optString("type")

        return when {
            type.startsWith("candle.") -> parseCandle(obj)?.let { ParsedUpbitWs.Candle(it) }
            type == "trade" -> parseTrade(obj)?.let { ParsedUpbitWs.Trade(it) }
            type == "ticker" -> parseTicker(obj)?.let { ParsedUpbitWs.Ticker(it) }
            else -> null
        }
    }

    // domain/repository/ChartRepository.kt 내부

    private fun parseTicker(obj: JSONObject): UpbitTicker? {
        val market = obj.optString("code")
        if (market.isBlank()) return null

        val tradePrice = obj.optDouble("trade_price", Double.NaN)
        val signedChangeRate = obj.optDouble("signed_change_rate", Double.NaN)
        val accVol = obj.optDouble("acc_trade_volume_24h", 0.0)

        // ✅ 추가 파싱
        val accPrice = obj.optDouble("acc_trade_price_24h", 0.0)
        val high = obj.optDouble("high_price", 0.0)
        val low = obj.optDouble("low_price", 0.0)
        val prevClose = obj.optDouble("prev_closing_price", 0.0)

        if (tradePrice.isNaN() || signedChangeRate.isNaN()) return null

        return UpbitTicker(
            market = market,
            tradePrice = tradePrice,
            signedChangeRate = signedChangeRate,
            accTradeVolume24h = accVol,
            // ✅ 생성자에 추가 전달
            accTradePrice24h = accPrice,
            highPrice = high,
            lowPrice = low,
            prevClosingPrice = prevClose
        )
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
            time = timeSec, open = open, high = high, low = low, close = close,
            volume = volume, volumeColor = volumeColor
        )
    }

    private fun parseTrade(obj: JSONObject): RecentTrade? {
        val market = obj.optString("code")
        if (market.isBlank()) return null
        val tsMs = if (obj.has("trade_timestamp")) obj.optLong("trade_timestamp", 0L) else obj.optLong("timestamp", 0L)
        if (tsMs <= 0L) return null
        val price = obj.optDouble("trade_price", Double.NaN)
        val volume = obj.optDouble("trade_volume", Double.NaN)
        val askBid = obj.optString("ask_bid")
        if (price.isNaN() || volume.isNaN() || askBid.isBlank()) return null
        val color = if (askBid == "BID") "#EF2B2A" else "#2569F2"

        return RecentTrade(
            market = market, timestampMs = tsMs, price = price,
            volume = volume, askBid = askBid, color = color
        )
    }
}