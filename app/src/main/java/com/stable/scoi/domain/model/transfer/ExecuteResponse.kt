package com.stable.scoi.domain.model.transfer

import kotlinx.serialization.Serializable

@Serializable
data class ExecuteResponse (
    val transferId: Long,
    val status: String = "",
    val uuid: String = "",
    val exchangeTxid: String = "",
    val state: String = "",
    val requestedAt: String = ""
)