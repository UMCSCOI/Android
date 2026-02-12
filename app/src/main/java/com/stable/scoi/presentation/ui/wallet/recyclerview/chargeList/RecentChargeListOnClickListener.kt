package com.stable.scoi.presentation.ui.wallet.recyclerview.chargeList

import com.stable.scoi.domain.model.wallet.TransactionsCharge

interface RecentChargeListOnClickListener {
    fun RCLOnClickListener(recentChargeData: TransactionsCharge)
    fun cancelOnclickListener(cancelData: TransactionsCharge)
}