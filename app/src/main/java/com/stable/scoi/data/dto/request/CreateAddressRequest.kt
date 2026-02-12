package com.stable.scoi.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateAddressRequest(
    val exchangeType: String,     // BITHUMB, UPBIT
    val coinType: List<String>,   // ["USDC", "USDT"]
    val netType: List<String>     // ["ETH", "TRX"]
)