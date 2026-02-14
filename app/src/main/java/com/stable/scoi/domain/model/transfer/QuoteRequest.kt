package com.stable.scoi.domain.model.transfer

import kotlinx.serialization.Serializable

@Serializable
data class QuoteRequest(
    val available: String,
    val amount: String,
    val coinType: String,
    val networkType: String,
    val networkFee: String
)
