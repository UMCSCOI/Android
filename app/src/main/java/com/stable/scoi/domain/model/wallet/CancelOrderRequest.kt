package com.stable.scoi.domain.model.wallet

import kotlinx.serialization.Serializable

@Serializable
data class CancelOrderRequest(
    val tradeType: String = "",
    val uuid: String = "",
    val txid: String? = null,
)
