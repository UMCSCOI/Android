package com.stable.scoi.domain.model.transfer

data class Receiver(
    var receiverKORName: String? = null,
    var receiverENGName: String? = null,
    var receiverAddress: String? = null,
    var receiverType: ReceiverType = ReceiverType.Empty,
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

data class BookMarkReceiver(
    var receiverName: String? = null,
    var receiverAddress: String? = null,
    var exchangeType: String? = null
)

