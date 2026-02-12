package com.stable.scoi.domain.model.transfer

data class QuoteRequest(
    val available: String,
    val amount: String,
    val coinType: String,
    val networkType: String,
    val networkFee: String
)
