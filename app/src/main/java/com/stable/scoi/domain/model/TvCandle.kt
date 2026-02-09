package com.stable.scoi.domain.model

data class TvCandle(
    val time: Long,          // epoch seconds
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double,
    val volumeColor: String, // "#ff4d4f" or "#2f7cff"
)

data class RecentTrade(
    val market: String,
    val timestampMs: Long,
    val price: Double,
    val volume: Double,
    val askBid: String,      // "ASK" or "BID"
    val color: String,       // UI용: BID=빨강, ASK=파랑(원하면 반대로 바꿔도 됨)
)

data class UpbitTicker(
    val market: String,
    val tradePrice: Double,       // 현재가
    val signedChangeRate: Double, // 등락률 (예: 0.05 = 5%)
    val accTradeVolume24h: Double, // (선택) 24시간 거래량 등
    val accTradePrice24h: Double, // 거래대금(24H)
    val highPrice: Double,        // 고가
    val lowPrice: Double,         // 저가
    val prevClosingPrice: Double  // 전일종가
)