package com.stable.scoi.domain.model

sealed class UpbitWsEvent {
    data class Open(val code: Int) : UpbitWsEvent()
    data class Message(val rawJson: String) : UpbitWsEvent()
    data class Closing(val code: Int, val reason: String) : UpbitWsEvent()
    data class Failure(val message: String, val httpCode: Int?) : UpbitWsEvent()

}

sealed interface ParsedUpbitWs {
    data class Candle(val value: TvCandle) : ParsedUpbitWs
    data class Trade(val value: RecentTrade) : ParsedUpbitWs
    data class Ticker(val value: UpbitTicker) : ParsedUpbitWs
}
