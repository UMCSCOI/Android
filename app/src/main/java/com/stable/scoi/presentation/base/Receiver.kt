package com.stable.scoi.presentation.base

data class Receiver(
    var receiverName: String? = null,
    var receiverAddress: String? = null,
    var receiverType: ReceiverType = ReceiverType.Empty
)

data class Information(
    var exchangeType: String = "",
    var assetSymbol: String = "",
    var amount: String = "",
)

data class Execute(
    var simplePassword: String = ""
)

sealed class ReceiverType {
    object Individual: ReceiverType()
    object Corporation: ReceiverType()
    object Empty: ReceiverType()
}

