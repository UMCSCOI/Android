package com.stable.scoi.presentation.ui.wallet.bottomsheet

interface SetExchangeType {
    fun upbit()
    fun bithumb()
    fun empty()
}

interface SetArraySettingCharge {
    fun arraySettingCharge(
        sortType: String,
        categoryType: String,
        statusType: String,
        periodType: String
    )
}

interface SetArraySettingTransfer {
    fun arraySettingTransfer(
        sortType: String,
        categoryType: String,
        periodType: String
    )
}