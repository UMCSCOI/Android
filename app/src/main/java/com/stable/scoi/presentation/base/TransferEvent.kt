package com.stable.scoi.presentation.base

sealed class TransferEvent : UiEvent  {
    object NavigateToNextPage: TransferEvent()
    object OpenReceiverType: TransferEvent()
}