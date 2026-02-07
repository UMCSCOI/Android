package com.stable.scoi.presentation.ui.transfer.bottomsheet

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
    fun empty()
}

interface SetNetworkType {
    fun networkType(network: Network)
}



