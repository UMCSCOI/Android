package com.stable.scoi.data.api.transfer

import com.stable.scoi.data.base.ApiResponse
import com.stable.scoi.domain.model.transfer.BalancesResponse
import com.stable.scoi.domain.model.transfer.DirectoryListResponse
import com.stable.scoi.domain.model.transfer.ExecuteRequest
import com.stable.scoi.domain.model.transfer.ExecuteResponse
import com.stable.scoi.domain.model.transfer.QuoteRequest
import com.stable.scoi.domain.model.transfer.QuoteResponse
import com.stable.scoi.domain.model.transfer.ValidateRequest
import com.stable.scoi.domain.model.transfer.ValidateResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DirectoryListAPI {
    @GET("/api/transfers/recipients")
    suspend fun loadDirectoryList(
        @Query("exchangeType") exchange: String,
        @Query("coinType") coinType: String
    ) : ApiResponse<DirectoryListResponse>
}

interface RecipientValidateAPI {
    @POST("/api/transfers/recipients/validate")
    suspend fun judgeValidate(
        @Body request: ValidateRequest
    ) : ApiResponse<ValidateResponse>
}

interface ExecuteAPI {
    @POST("/api/transfers/execute")
    suspend fun execute(
        @Body request: ExecuteRequest
    ) : ApiResponse<ExecuteResponse>
}

interface QuoteAPI {
    @POST("/api/transfers/quotes")
    suspend fun loadQuote(
        @Body request: QuoteRequest
    ) : ApiResponse<QuoteResponse>

}

interface BalancesAPI {
    @GET("/api/balances")
    suspend fun loadBalances(
        @Query("tradeType") tradeType: String
    ) : ApiResponse<BalancesResponse>
}