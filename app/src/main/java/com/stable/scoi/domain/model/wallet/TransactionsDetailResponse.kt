package com.stable.scoi.domain.model.wallet

import kotlinx.serialization.Serializable

@Serializable
data class TransactionsDetailResponse (
    val category: String = "",
    val remitDetail: RemitDetail,
    val topupDetail: TopupDetail,
)

data class RemitDetail (
    val type: String = "",
    val uuid: String = "",
    val currency: String = "",
    val netType: String = "",
    val txid: String = "",
    val state: String = "",
    val createdAt: String = "",
    val doneAt: String = "",
    val amount: String = "",
    val fee: String = "",
    val transactionType: String = ""
)

data class TopupDetail (
    val uuid: String = "",
    val market: String = "",
    val side: String = "",
    val ordType: String = "",
    val price: String = "",
    val state: String = "",
    val createAt: String = "",
    val volume: String = "",
    val remainingVolume: String = "",
    val executedVolume: String = "",
    val reservedFee: String = "",
    val remainingFee: String = "",
    val paidFee: String = "",
    val locked: String = "",
    val tradesCount: String = "",
    val trades: Trades = Trades()
)

data class Trades (
    val market: String = "",
    val uuid: String = "",
    val price: String = "",
    val volume: String = "",
    val funds: String = "",
    val side: String = "",
    val createAt: String = ""
)