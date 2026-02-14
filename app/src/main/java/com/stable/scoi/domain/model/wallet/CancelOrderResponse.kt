package com.stable.scoi.domain.model.wallet

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class CancelOrderResponse(
    val uuid: String = "",
    val txid: String = "",
    val createdAt: LocalDateTime? = null
)
