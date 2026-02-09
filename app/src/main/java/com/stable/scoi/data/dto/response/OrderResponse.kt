package com.stable.scoi.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class OrderResponse(
    val uuid: String,
    val txid: String? = null, // 예시에는 있으나 명세에는 없는 경우 고려
    val market: String,
    val side: String,
    val orderType: String,
    val createdAt: String     // "2026-01-10T19:51:25+09:00"
)