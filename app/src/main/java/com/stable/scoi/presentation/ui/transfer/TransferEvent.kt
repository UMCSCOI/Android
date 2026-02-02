package com.stable.scoi.presentation.ui.transfer

import com.stable.scoi.presentation.base.UiEvent

sealed class TransferEvent : UiEvent {
    object NavigateToNextPage: TransferEvent()
    object OpenReceiverType: TransferEvent()
}