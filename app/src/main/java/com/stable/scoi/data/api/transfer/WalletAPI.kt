package com.stable.scoi.data.api.transfer

import com.stable.scoi.data.base.ApiResponse
import com.stable.scoi.domain.model.transfer.DirectoryListResponse
import com.stable.scoi.domain.model.wallet.CancelOrderRequest
import com.stable.scoi.domain.model.wallet.CancelOrderResponse
import com.stable.scoi.domain.model.wallet.TransactionsDetailResponse
import com.stable.scoi.domain.model.wallet.TransactionsRemitResponse
import com.stable.scoi.domain.model.wallet.TransactionsTopupsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface TransactionsRemitAPI {
    @GET("/api/mywallet/transactions/remit")
    suspend fun loadTransactionsRemit(
        @Query("exchangeType") exchange: String,
        @Query("type") type: String,
        @Query("period") period: String,
        @Query("order") order: String,
        @Query("limit") limit: Int
    ) : ApiResponse<TransactionsRemitResponse>
}

interface TransactionsTopupsAPI {
    @GET("/api/mywallet/transactions/topups")
    suspend fun loadTransactionsTopups(
        @Query("exchangeType") exchange: String,
        @Query("type") type: String,
        @Query("state") state: String,
        @Query("period") period: String,
        @Query("order") order: String,
        @Query("limit") limit: Int
    ) : ApiResponse<TransactionsTopupsResponse>
}

interface TransactionsDetailAPI {
    @GET("/api/mywallet/transactions/detail")
    suspend fun loadTransactionsDetail(
        @Query("exchangeType") exchange: String,
        @Query("category") category: String,
        @Query("remitType") remitType: String?,
        @Query("uuid") uuid: String,
        @Query("currency") currency: String?
    ) : ApiResponse<TransactionsDetailResponse>
}


interface CancelOrderAPI {
    @DELETE("/api/orders")
    suspend fun cancelOrder(
        @Body request: CancelOrderRequest
    ): ApiResponse<CancelOrderResponse>
}