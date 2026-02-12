package com.stable.scoi.presentation.ui.transfer

import com.stable.scoi.domain.model.transfer.Balance
import com.stable.scoi.domain.model.transfer.Balances
import com.stable.scoi.domain.model.transfer.DirectoryResult
import com.stable.scoi.domain.model.transfer.Recipient
import com.stable.scoi.domain.model.transfer.ValidateRequest
import com.stable.scoi.presentation.base.UiState

data class TransferState(
    val directoryList: ArrayList<DirectoryResult> = ArrayList(),
    val balances: List<Balances> = emptyList(),
    val nextCursor: String? = null,
    val hasNextCursor: Boolean = false,
    val validateBalance: Balance = Balance(),
    val isLoading: Boolean = false
) : UiState

