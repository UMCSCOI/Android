package com.stable.scoi.presentation.base

data class Receiver(
    var receiverName: String? = null,
    var receiverAddress: String? = null,
    var receiverType: ReceiverType = ReceiverType.Null
)

data class Information(
    var exchangeType: String = "",
    var assetSymbol: String = "",
    var amount: String = ""
)

sealed class ReceiverType {
    object Individual: ReceiverType()
    object Corporation: ReceiverType()
    object Null: ReceiverType()
}

