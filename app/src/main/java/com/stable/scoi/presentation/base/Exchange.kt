package com.stable.scoi.presentation.base

sealed class Exchange {
    object Upbit: Exchange()
    object Bithumb: Exchange()
    object Binance: Exchange()
    object Null: Exchange()
    object Unselected: Exchange()
}

sealed class AssetSymbol {
    object USDT: AssetSymbol()
    object USDC: AssetSymbol()
    object Null: AssetSymbol()
}