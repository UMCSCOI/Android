package com.stable.scoi.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class OrderTestRequest(
    val exchangeType: String, // 거래소 타입 (Bithumb, Upbit)
    val market: String,       // 마켓 (예: KRW-BTC)
    val side: String,         // 주문 종류 (bid: 매수, ask: 매도)
    val orderType: String,     // 주문 방식 (limit: 지정가 등)
    val price: String,        // 주문 가격
    val volume: String        // 주문 수량
)