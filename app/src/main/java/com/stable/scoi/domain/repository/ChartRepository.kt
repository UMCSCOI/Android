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
    fun streamMarket(
        market: String,
        unitMinutes: Int,
        initialCount: Int
    ): Flow<CandleStreamEvent> = channelFlow {

        // 1) REST API로 초기 분봉 데이터 로드 (차트용 스냅샷)
        try {
            val snapshot = rest.getMinuteCandles(
                unit = unitMinutes,
                market = market,
                count = initialCount.coerceAtMost(200),
            )
                .asReversed() // 과거 -> 최신 순 정렬
                .map { it.toTvCandle() }

            send(CandleStreamEvent.Snapshot(snapshot))

        } catch (e: Exception) {
            SLOG.D("Repo: REST Snapshot error $e")
            // 스냅샷 실패해도 소켓 연결은 시도하도록 진행
        }

        // 2) WS 통합 스트림 연결 (candle + trade + ticker)
        val wsJob = launch {
            wsApi.streamMinuteCandle(
                markets = listOf(market),
                unitMinutes = unitMinutes,
                subscribeCandle = true, // 차트 갱신용
                subscribeTrade = true,  // 체결 내역용
                subscribeTicker = true  // ✅ 현재가 및 등락률용
            )
                .catch { e ->
                    SLOG.D("Repo: WS error $e")
                }
                .collect { ev ->
                    when (ev) {
                        is UpbitWsEvent.Message -> {
                            // JSON 파싱
                            val parsed = parseUpbitWsMessage(ev.rawJson, expectedMarket = market)
                                ?: return@collect

                            // 파싱 결과에 따라 이벤트 분기 처리
                            when (parsed) {
                                is ParsedUpbitWs.Candle ->
                                    send(CandleStreamEvent.Update(parsed.value))

                                is ParsedUpbitWs.Trade ->
                                    send(CandleStreamEvent.TradeUpdate(parsed.value))

                                is ParsedUpbitWs.Ticker -> // ✅ Ticker 처리 추가
                                    send(CandleStreamEvent.TickerUpdate(parsed.value))
                            }
                        }

                        is UpbitWsEvent.Failure -> {
                            SLOG.D("Repo: WS failure=${ev.message}")
                        }

                        is UpbitWsEvent.Closing -> {
                            SLOG.D("Repo: WS closing=${ev.reason}")
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
    private fun parseUpbitWsMessage(
        rawJson: String,
        expectedMarket: String? = null, // ✅ 수정 1: Nullable로 변경 (null이면 모든 마켓 허용)
    ): ParsedUpbitWs? {
        val obj = runCatching { JSONObject(rawJson) }.getOrNull() ?: return null

        val type = obj.optString("type") // "candle.1m", "trade", "ticker"
        val code = obj.optString("code")

        // ✅ 수정 2: expectedMarket이 "지정되었는데(not null)" 코드가 다를 때만 거름.
        // (null인 경우는 검사하지 않고 통과시킴)
        if (expectedMarket != null && code != expectedMarket) return null

        return when {
            type.startsWith("candle.") -> {
                parseCandle(obj)?.let { ParsedUpbitWs.Candle(it) }
            }

            type == "trade" -> {
                parseTrade(obj)?.let { ParsedUpbitWs.Trade(it) }
            }

            type == "ticker" -> { // Ticker 파싱
                parseTicker(obj)?.let { ParsedUpbitWs.Ticker(it) }
            }

            else -> null
        }
    }
    // --- Parsing Logic Methods ---

    /**
     * ✅ Ticker(현재가) 파싱 로직
     */
    private fun parseTicker(obj: JSONObject): UpbitTicker? {
        val market = obj.optString("code")
        if (market.isBlank()) return null

        val tradePrice = obj.optDouble("trade_price", Double.NaN)
        val signedChangeRate = obj.optDouble("signed_change_rate", Double.NaN) // 예: 0.05
        val accVol = obj.optDouble("acc_trade_volume_24h", 0.0)

        if (tradePrice.isNaN() || signedChangeRate.isNaN()) return null

        return UpbitTicker(
            market = market,
            tradePrice = tradePrice,
            signedChangeRate = signedChangeRate,
            accTradeVolume24h = accVol
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
        val close = obj.optDouble("trade_price", Double.NaN) // 캔들에서는 현재가가 trade_price
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

        // 타임스탬프 처리
        val tsMs = when {
            obj.has("trade_timestamp") -> obj.optLong("trade_timestamp", 0L)
            obj.has("timestamp") -> obj.optLong("timestamp", 0L)
            else -> 0L
        }
        if (tsMs <= 0L) return null

        val price = obj.optDouble("trade_price", Double.NaN)
        val volume = obj.optDouble("trade_volume", Double.NaN)
        if (price.isNaN() || volume.isNaN()) return null

        val askBid = obj.optString("ask_bid") // "ASK" or "BID"
        if (askBid.isBlank()) return null

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

    fun streamTickers(markets: List<String>): Flow<CandleStreamEvent> = channelFlow {
        // 1. 소켓 연결 (리스트를 통째로 넘김)
        val wsJob = launch {
            wsApi.streamMinuteCandle(
                markets = markets,          // ["KRW-USDT", "KRW-USDC"]
                unitMinutes = 1,
                subscribeCandle = false,    // 캔들 필요 없으면 false
                subscribeTrade = false,     // 체결 내역 필요 없으면 false
                subscribeTicker = true      // ✅ 현재가(Ticker) 필수
            )
                .catch { e -> SLOG.D("Repo: WS error $e") }
                .collect { ev ->
                    if (ev is UpbitWsEvent.Message) {
                        // Ticker 파싱
                        val parsed = parseUpbitWsMessage(ev.rawJson, expectedMarket = null) // null이면 모든 마켓 허용

                        // 파싱된 데이터 방출
                        if (parsed is ParsedUpbitWs.Ticker) {
                            send(CandleStreamEvent.TickerUpdate(parsed.value))
                        }
                    }
                }
        }

        awaitClose {
            wsJob.cancel()
            wsApi.close()
        }
    }
}