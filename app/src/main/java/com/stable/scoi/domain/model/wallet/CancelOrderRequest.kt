package com.stable.scoi.domain.model.wallet

data class CancelOrderRequest(
    val tradeType: String = "",
    val uuid: String = "",
    val txid: String? = null,
)
