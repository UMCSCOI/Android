package com.stable.scoi.domain.model.transfer

import kotlinx.serialization.Serializable

@Serializable
data class ValidateRequest(
    val memberType: String = "INDIVIDUAL",
    val recipientKoName: String = "",
    val recipientEnName: String = "",
    val walletAddress: String = "",
    val exchangeType: String = "",
    val coinType: String = "",
    val netType: String = "TRX"
)
