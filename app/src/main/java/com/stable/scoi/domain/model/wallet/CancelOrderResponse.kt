package com.stable.scoi.domain.model.wallet

import java.time.LocalDateTime

data class CancelOrderResponse(
    val uuid: String = "",
    val txid: String = "",
    val createdAt: LocalDateTime? = null
)
