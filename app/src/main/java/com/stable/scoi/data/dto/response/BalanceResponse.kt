package com.stable.scoi.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class BalanceResponse(
    val currency: String, // 통화 코드 (예: "KRW", "BTC")
    val balance: String,  // 계좌 잔고 (가용 잔액 + 잠긴 잔액)
    val locked: String    // 주문/출금 대기 중인 잔액
)