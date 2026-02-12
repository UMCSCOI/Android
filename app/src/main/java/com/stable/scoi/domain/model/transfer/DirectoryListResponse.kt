package com.stable.scoi.domain.model.transfer

import kotlinx.serialization.Serializable

@Serializable
data class DirectoryListResponse(
    val result: ArrayList<DirectoryResult>
)

data class DirectoryResult (
    val memberType: String = "INDIVIDUAL",
    val recipientKoName: String = "",
    val recipientEnName: String = "",
    val walletAddress: String = "",
    val exchangeType: String = "",
    val currency: String = "",
    val netType: String = ""
)
