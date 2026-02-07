package com.stable.scoi.presentation.ui.transfer

data class Directory(
    val recipientId: String = "",
    val recipientKORName: String = "",
    val recipientENGName: String = "",
    val walletAddress: String = "",
    val exchangeType: String = "",
    val isFavorite: Boolean
)
