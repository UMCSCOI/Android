package com.stable.scoi.presentation.base

sealed class TransferEvent : UiEvent  {
    object Submit: TransferEvent()
    object Cancel: TransferEvent()
    object Null: TransferEvent()
}