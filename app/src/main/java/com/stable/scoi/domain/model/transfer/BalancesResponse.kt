package com.stable.scoi.domain.model.transfer

import kotlinx.serialization.Serializable

@Serializable
data class BalancesResponse (
    val balances: List<Balances>
)

data class Balances (
    val currency: String = "",
    val balance: String = "",
    val locked: String = ""
)