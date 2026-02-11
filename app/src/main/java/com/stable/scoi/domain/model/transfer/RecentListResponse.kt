package com.stable.scoi.domain.model.transfer

import kotlinx.serialization.Serializable

@Serializable
data class RecentListResponse(
    val recipientId: Int,
    val recipientType: String,
    val recipientName: String,
    val corpKoreanName: String,
    val walletAddress: String,
    val exchangeType: String,
    val network: String,
    val isFavorite: Boolean
)
