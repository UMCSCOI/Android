package com.stable.scoi.presentation.base

data class Receiver(
    var receiverName: String? = null,
    var receiverAddress: String? = null,
    var receiverType: ReceiverType = ReceiverType.Null
)

sealed class ReceiverType {
    object Individual: ReceiverType()
    object Corporation: ReceiverType()
    object Null: ReceiverType()
}

