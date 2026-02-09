package com.stable.scoi.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class OrderInfoResponse(
    val balance: String = "",
    val maxQuantity: String = "",
)