package com.stable.scoi.domain.model.transfer

import kotlinx.serialization.Serializable

@Serializable
data class QuoteResponse(
    val amount: String = "",
    val networkFee: String = "",
    val totalAmount: String = ""
)
