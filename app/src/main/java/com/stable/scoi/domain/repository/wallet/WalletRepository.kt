package com.stable.scoi.domain.repository.wallet

import com.stable.scoi.data.api.transfer.CancelOrderAPI
import com.stable.scoi.data.api.transfer.DirectoryListAPI
import com.stable.scoi.data.api.transfer.TransactionResponse
import com.stable.scoi.data.api.transfer.TransactionsDetailAPI
import com.stable.scoi.data.api.transfer.TransactionsRemitAPI
import com.stable.scoi.data.api.transfer.TransactionsTopupsAPI
import com.stable.scoi.data.api.transfer.WithDrawRequest
import com.stable.scoi.data.base.ApiState
import com.stable.scoi.data.base.apiCall
import com.stable.scoi.domain.model.transfer.DirectoryListResponse
import com.stable.scoi.domain.model.wallet.CancelOrderRequest
import com.stable.scoi.domain.model.wallet.CancelOrderResponse
import com.stable.scoi.domain.model.wallet.TransactionsDetailResponse
import com.stable.scoi.domain.model.wallet.TransactionsRemitResponse
import com.stable.scoi.domain.model.wallet.TransactionsTopupsResponse
import javax.inject.Inject


class TransactionsRemitRepository @Inject constructor(
    private val api: TransactionsRemitAPI,
    private val api2: DirectoryListAPI
) {
    suspend fun loadTransactionsRemit(
        exchangeType: String,
        type: String,
        period: String,
        order: String,
        limit: Int
    ): ApiState<TransactionsRemitResponse> {
        return apiCall { api.loadTransactionsRemit(exchangeType, type, period, order, limit) }
    }

    suspend fun withDraw(
        request: WithDrawRequest
    ): ApiState<TransactionResponse> {
        return apiCall { api2.withdrawKrw(request) }
    }

    suspend fun diposit(
        request: WithDrawRequest
    ): ApiState<TransactionResponse> {
        return apiCall { api2.dipositsKrw(request) }
    }
}


class TransactionsTopupsRepository @Inject constructor(private val api: TransactionsTopupsAPI) {
    suspend fun loadTransactionsTopups(
        exchangeType: String,
        type: String,
        state: String,
        period: String,
        order: String,
        limit: Int
    ): ApiState<TransactionsTopupsResponse> {
        return apiCall { api.loadTransactionsTopups(exchangeType, type, state, period, order, limit) }
    }
}


class TransactionsDetailRepository @Inject constructor(private val api: TransactionsDetailAPI) {
    suspend fun loadTransactionsDetail(
        exchangeType: String,
        category: String,
        remitType: String?,
        uuid: String,
        currency: String?
    ): ApiState<TransactionsDetailResponse> {
        return apiCall { api.loadTransactionsDetail(exchangeType,category,remitType,uuid,currency ) }
    }
}


class CancelOrderRepository @Inject constructor(private val api: CancelOrderAPI) {
    suspend fun cancelOrder(request: CancelOrderRequest): ApiState<CancelOrderResponse> {
        return apiCall { api.cancelOrder(request) }
    }
}