package com.stable.scoi.domain.model.transfer

import kotlinx.serialization.Serializable

@Serializable
data class ExecuteRequest (
    val currency: String = "",
    val netType: String = "",
    val amount: String = "",
    val address: String = "",
    val exchangeType: String = "",
    val receiverType: String = "",
    val receiverKoName: String = "",
    val receiverEnName: String = "",
    val simplePassword: String = "",
    val idempotentKey: String = ""
)