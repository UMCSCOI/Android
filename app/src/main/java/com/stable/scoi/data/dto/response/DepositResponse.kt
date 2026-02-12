package com.stable.scoi.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class DepositResponse(
    val currency: String, // "KRW" 고정
    val uuid: String,     // 특정 입금 주문의 UUID
    val txid: String      // 특정 입금 주문의 트랜잭션 ID
)