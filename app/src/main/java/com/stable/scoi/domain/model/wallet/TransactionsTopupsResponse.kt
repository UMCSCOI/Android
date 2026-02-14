package com.stable.scoi.domain.model.wallet

import kotlinx.serialization.Serializable

@Serializable
data class TransactionsTopupsResponse (
    val transactions: List<TransactionsCharge>,
    val totalCount: Int
)

data class TransactionsCharge (
    val uuid: String = "",
    val market: String = "",
    val side: String = "",
    val state: String = "",
    val createdAt: String = "",
    val volume: String = "",
    val executedVolume: String = ""
)