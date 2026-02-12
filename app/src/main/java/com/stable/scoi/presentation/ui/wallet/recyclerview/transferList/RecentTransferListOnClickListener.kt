package com.stable.scoi.presentation.ui.wallet.recyclerview.transferList

import com.stable.scoi.domain.model.wallet.Transactions

interface RecentTransferListOnClickListener {
    fun RTLOnClickListener(recentTransferData: Transactions)
}