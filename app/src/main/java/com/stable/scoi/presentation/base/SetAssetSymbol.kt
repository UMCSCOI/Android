package com.stable.scoi.presentation.base

interface SetAssetSymbol {
    fun typeUSDT()
    fun typeUSDC()
}

interface SetReceiverType {
    fun individual()
    fun corporation()
}

interface SetExchangeType {
    fun upbit()
    fun bithumb()
    fun binance()
    fun empty()
}