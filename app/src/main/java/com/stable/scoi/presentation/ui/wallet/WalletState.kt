package com.stable.scoi.presentation.ui.wallet

import com.stable.scoi.domain.model.transfer.Balances
import com.stable.scoi.domain.model.wallet.CancelOrderRequest
import com.stable.scoi.domain.model.wallet.CancelOrderResponse
import com.stable.scoi.domain.model.wallet.RemitDetail
import com.stable.scoi.domain.model.wallet.TopupDetail
import com.stable.scoi.domain.model.wallet.Transactions
import com.stable.scoi.domain.model.wallet.TransactionsCharge
import com.stable.scoi.domain.model.wallet.TransactionsDetailResponse
import com.stable.scoi.domain.model.wallet.TransactionsRemitResponse
import com.stable.scoi.presentation.base.UiState

data class WalletState(
    val transactionsRemitItems: List<Transactions> = emptyList(),
    val transactionsTopupsItems: List<TransactionsCharge> = emptyList(),
    val transactionsRemitDetailItem: RemitDetail = RemitDetail(),
    val transactionsTopupsDetailItem: TopupDetail = TopupDetail(),
    val cancelOrderState: String = "",
    val balances: List<Balances> = emptyList(),
    val totalCount: Int = 20
): UiState