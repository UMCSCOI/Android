package com.stable.scoi.domain.model

sealed interface CandleStreamEvent {
    data class Snapshot(val candles: List<TvCandle>) : CandleStreamEvent
    data class Update(val candle: TvCandle) : CandleStreamEvent
    data class TradeUpdate(val trade: RecentTrade) : CandleStreamEvent
}