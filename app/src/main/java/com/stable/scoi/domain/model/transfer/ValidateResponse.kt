package com.stable.scoi.domain.model.transfer

import kotlinx.serialization.Serializable

@Serializable
data class ValidateResponse(
    val recipient: Recipient,
    val balance: Balance,
)

data class Recipient(
    val memberType: String = "INDIVIDUAL",
    val recipientKoName: String = "",
    val recipientEnName: String = "",
    val walletAddress: String = ""
)

data class Balance(
    val exchangeType: String = "",
    val assetSymbol: String = "",
    val availableAmount: String = "",
    val updateAt: String = ""
)
