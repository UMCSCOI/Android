package com.stable.scoi.presentation.ui.transfer

data class BookMark(
    val recipientId: String = "",
    val recipientName: String = "",
    val walletAddress: String = "",
    val exchangeType: String = "",
    val isFavorite: Boolean
)

data class Recent(
    val recipientId: String = "",
    val recipientName: String = "",
    val walletAddress: String = "",
    val exchangeType: String = "",
    val isFavorite: Boolean
)
