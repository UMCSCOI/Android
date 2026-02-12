package com.stable.scoi.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class OrderRequest(
    val tradeType: String,  // 거래소 타입 (BITHUMB, UPBIT)
    val market: String,     // 마켓 타입 (ex. KRW-BTC)
    val side: String,       // 주문 타입 (bid: 매수, ask: 매도)
    val orderType: String,  // 주문 방식 (limit, price, market)
    val price: String,      // 주문 가격 (지정가->호가, 시장가 매수->총액)
    val volume: String,     // 주문 수량 (매도시 필수)
    val password: String    // 간편 비밀번호 (암호화된 상태)
)