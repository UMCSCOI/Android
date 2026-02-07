package com.stable.scoi.domain.model.transfer

data class Receiver(
    var receiverKORName: String? = null,
    var receiverENGName: String? = null,
    var receiverAddress: String? = null
)

data class Information(
    var exchangeType: String = "",
    var assetSymbol: String = "",
    var amount: String = "",
)

data class Execute(
    var simplePassword: String = ""
)

