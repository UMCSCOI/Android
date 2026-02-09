package com.stable.scoi.presentation.ui.wallet.recyclerview.transferList

import com.stable.scoi.presentation.ui.wallet.recyclerview.chargeList.Counterparty

data class RecentTransferList(
    val transactionId: String = "",
    val category: String = "",
    val amount: String = "",
    val fee: String = "",
    val exchangeType: String = "",
    val netAmount: String = "",
    val currency: String = "",
    val title: String = "",
    val counterparty: Counterparty = Counterparty(),
    val occurredAt: String = "",
    val assetSymbol: String = ""
)

data class Counterparty(
    val userId: String = "",
    val displayName: String = ""
)

