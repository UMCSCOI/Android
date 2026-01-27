package com.stable.scoi.domain.model

data class TvCandle(
    val time: Long,      // epoch seconds (TradingView/lightweight-charts 규격)
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double,
    val volumeColor: String, // 거래량 색(상승/하락)
)