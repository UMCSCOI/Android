package com.stable.scoi.domain.model.home

import com.stable.scoi.domain.model.enums.AccountType

data class AccountCard(
    val type: AccountType = AccountType.BITSUM,
    val usdt: String = "10,000",
    val usdc: String = "10,000",
    val key: String = "TXYZopYRdj2D9XRtbG411XZZc",
    val isEmpty: Boolean = true
)
