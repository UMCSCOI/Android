package com.stable.scoi.domain.repository

import com.stable.scoi.data.api.ChargeApi
import com.stable.scoi.data.base.ApiState
import com.stable.scoi.data.base.apiCall
import com.stable.scoi.data.dto.request.*
import com.stable.scoi.data.dto.response.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChargeRepository @Inject constructor(
    private val chargeApi: ChargeApi
) {
    // 계좌 잔고 조회
    suspend fun getMyBalances(tradeType: String): ApiState<BalanceListResponse> {
        return apiCall { chargeApi.getMyBalances(tradeType) }
    }

    // 주문 전 가능 금액 및 정보 조회
    suspend fun getOrderInfo(tradeType: String, coinType: String): ApiState<OrderInfoResponse> {
        return apiCall { chargeApi.getOrderInfo(tradeType, coinType) }
    }

    // 주문 가능 여부 확인 테스트
    suspend fun checkOrderAvailable(request: OrderTestRequest): ApiState<Unit> {
        return apiCall { chargeApi.checkOrderAvailable(request) }
    }

    // 코인 주문 생성
    suspend fun createOrder(request: OrderRequest): ApiState<OrderResponse> {
        return apiCall { chargeApi.createOrder(request) }
    }

    // 원화 충전 요청 (2차 인증)
    suspend fun requestDepositKrw(request: DepositRequest): ApiState<DepositResponse> {
        return apiCall { chargeApi.requestDepositKrw(request) }
    }

    // 코인 입금 주소 단건 조회
    suspend fun getDepositAddress(exchangeType: String): ApiState<String> {
        return apiCall { chargeApi.getDepositAddress(exchangeType) }
    }

    // 코인 입금 주소 생성 요청
    suspend fun createDepositAddress(request: CreateAddressRequest): ApiState<List<String>> {
        return apiCall { chargeApi.createDepositAddress(request) }
    }
}