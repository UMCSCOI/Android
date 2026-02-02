package com.stable.scoi.data.dto.response

import android.os.Build
import androidx.annotation.RequiresApi
import com.stable.scoi.domain.model.TvCandle
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class UpbitMinuteCandleDto(
    val market: String,
    val candle_date_time_utc: String,
    val opening_price: Double,
    val high_price: Double,
    val low_price: Double,
    val trade_price: Double,
    val candle_acc_trade_volume: Double,
) {
    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        private val UPBIT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

        @RequiresApi(Build.VERSION_CODES.O)
        fun UpbitMinuteCandleDto.toTvCandle(): TvCandle {
            val t = LocalDateTime.parse(candle_date_time_utc, UPBIT_FMT).toEpochSecond(ZoneOffset.UTC)
            val isUp = trade_price >= opening_price
            val volumeColor = if (isUp) "#ff4d4f" else "#2f7cff"

            return TvCandle(
                time = t,
                open = opening_price,
                high = high_price,
                low = low_price,
                close = trade_price,
                volume = candle_acc_trade_volume,
                volumeColor = volumeColor
            )
        }
    }
}