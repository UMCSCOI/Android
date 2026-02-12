package com.stable.scoi.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class DepositRequest(
    val exchangeType: String, // 거래소 타입 (UPBIT, BITHUMB)
    val amount: Long,         // 입금 금액
    val MFA: String           // 2차 인증서 타입 (KAKAO, NAVER, HANA)
)