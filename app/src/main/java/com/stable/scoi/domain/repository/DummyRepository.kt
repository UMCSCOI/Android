package com.stable.scoi.domain.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.stable.scoi.data.api.OkHttpUpbitCandleWsApi
import com.stable.scoi.data.api.UpbitQuotationRestApi
import com.stable.scoi.data.dto.response.UpbitMinuteCandleDto.Companion.toTvCandle
import com.stable.scoi.domain.model.CandleStreamEvent
import com.stable.scoi.domain.model.TvCandle
import com.stable.scoi.domain.model.UpbitWsEvent
import com.stable.scoi.util.SLOG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
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
    fun streamMinuteCandles(
        market: String,
        unitMinutes: Int,
        initialCount: Int
    ): Flow<CandleStreamEvent> = channelFlow {

        // 1) REST로 분봉 200개 먼저 받기 (최신→과거로 내려오는 경우가 많아서 reverse)
        val snapshot: List<TvCandle> = rest.getMinuteCandles(
            unit = unitMinutes,
            market = market,
            count = initialCount.coerceAtMost(200),
        ).asReversed()
            .map { it.toTvCandle() }

        send(CandleStreamEvent.Snapshot(snapshot))

        // 2) WS로 실시간 업데이트 계속 받기
        wsApi.streamMinuteCandle(unitMinutes, market).collect { ev ->
            when (ev) {
                is UpbitWsEvent.Message -> {
                    val candle = ev.rawJson.parseWsCandleOrNull(market) ?: return@collect
                    send(CandleStreamEvent.Update(candle))
                }
                else -> Unit
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun String.parseWsCandleOrNull(expectedMarket: String): TvCandle? {
        // 메시지가 candle이 아닐 수도 있으니 방어적으로 파싱
        val obj = runCatching { JSONObject(this) }.getOrNull() ?: return null
        if (obj.optString("type") != "candle") return null
        if (obj.optString("code") != expectedMarket) return null

        val utc = obj.getString("candle_date_time_utc")
        val open = obj.getDouble("opening_price")
        val high = obj.getDouble("high_price")
        val low = obj.getDouble("low_price")
        val close = obj.getDouble("trade_price")
        val volume = obj.getDouble("candle_acc_trade_volume")

        val t = upbitUtcToEpochSeconds(utc)

        val isUp = close >= open
        val volumeColor = if (isUp) "#ff4d4f" else "#2f7cff"

        return TvCandle(
            time = t,
            open = open,
            high = high,
            low = low,
            close = close,
            volume = volume,
            volumeColor = volumeColor
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

//
//    suspend fun test() {
//        privateWsApi.stream("CdNpXH9T7CuDhPqXDngAeraClaRa1AE2htKOSRfr", "nxnlIRX2MKI9Y0Fgdc0EObhlbv8kVgpUJFLlbfxV", listOf("USDT-BTC", "USDT-ETH")).collect { event ->
//            SLOG.D("event = $event")
//        }
//    }
//
//    suspend fun test2() {
//        privateWsApi.stream("CdNpXH9T7CuDhPqXDngAeraClaRa1AE2htKOSRfr", "nxnlIRX2MKI9Y0Fgdc0EObhlbv8kVgpUJFLlbfxV", listOf("KRW-USDC", "USDT-USDC")).collect { event ->
//            SLOG.D("event = $event")
//        }
//    }
}