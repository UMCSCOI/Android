package com.stable.scoi.data.api

import com.stable.scoi.data.base.ApiResponse
import com.stable.scoi.data.dto.request.CreateAddressRequest
import com.stable.scoi.data.dto.request.DepositRequest
import com.stable.scoi.data.dto.request.OrderRequest
import com.stable.scoi.data.dto.request.OrderTestRequest
import com.stable.scoi.data.dto.response.BalanceResponse
import com.stable.scoi.data.dto.response.DepositResponse
import com.stable.scoi.data.dto.response.OrderInfoResponse
import com.stable.scoi.data.dto.response.OrderResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChargeApi {
    @GET("/api/balances")
    suspend fun getMyBalances(
        @Query("tradeType") tradeType: String
    ): ApiResponse<BalanceResponse>

    @GET("/api/orders/info")
    suspend fun getOrderInfo(
        @Query("tradeType") tradeType: String,
        @Query("coinType") coinType: String
    ): ApiResponse<OrderInfoResponse>

    @POST("/api/orders/test")
    suspend fun checkOrderAvailable(
        @Body request: OrderTestRequest
    ): ApiResponse<Unit>

    @POST("/api/orders")
    suspend fun createOrder(
        @Body request: OrderRequest
    ): ApiResponse<OrderResponse>

    @POST("/api/deposits/krw")
    suspend fun requestDepositKrw(
        @Body request: DepositRequest
    ): ApiResponse<DepositResponse>

    @GET("/api/deposits/address")
    suspend fun getDepositAddress(
        @Query("exchangeType") exchangeType: String
    ): ApiResponse<String>

    @POST("/api/deposits/address")
    suspend fun createDepositAddress(
        @Body request: CreateAddressRequest
    ): ApiResponse<List<String>>
}