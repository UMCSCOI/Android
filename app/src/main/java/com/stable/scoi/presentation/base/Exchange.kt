package com.stable.scoi.presentation.base

sealed class Exchange {
    object Upbit: Exchange()
    object Bithumb: Exchange()
    object Binance: Exchange()
}