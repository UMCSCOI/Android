package com.stable.scoi.domain.model.wallet

data class TransactionsRemitResponse (
    val transactions: List<Transactions>,
    val totalCount: Int
)

data class Transactions (
    val type: String = "",
    val uuid: String = "",
    val currency: String = "",
    val state: String = "",
    val amount: String = "",
    val fee: String = "",
    val txid: String = "",
    val createdAt: String= "",
    val doneAt: String = "",
    val transactionType: String? = null,
    val balance: String = ""
)