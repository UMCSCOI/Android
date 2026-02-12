package com.stable.scoi.domain.repository.transfer

import com.stable.scoi.data.api.transfer.BalancesAPI
import com.stable.scoi.data.api.transfer.DirectoryListAPI
import com.stable.scoi.data.api.transfer.ExecuteAPI
import com.stable.scoi.data.api.transfer.QuoteAPI
import com.stable.scoi.data.api.transfer.RecipientValidateAPI
import com.stable.scoi.data.base.ApiState
import com.stable.scoi.data.base.apiCall
import com.stable.scoi.domain.model.transfer.BalancesResponse
import com.stable.scoi.domain.model.transfer.DirectoryListResponse
import com.stable.scoi.domain.model.transfer.ExecuteRequest
import com.stable.scoi.domain.model.transfer.ExecuteResponse
import com.stable.scoi.domain.model.transfer.QuoteRequest
import com.stable.scoi.domain.model.transfer.QuoteResponse
import com.stable.scoi.domain.model.transfer.ValidateRequest
import com.stable.scoi.domain.model.transfer.ValidateResponse
import javax.inject.Inject

class DirectoryRepository @Inject constructor(private val api: DirectoryListAPI) {
    suspend fun loadDirectoryList(exchangeType: String, coinType: String): ApiState<DirectoryListResponse> {
        return apiCall { api.loadDirectoryList(exchangeType, coinType) }
    }
}

class ValidateRepository @Inject constructor(private val api: RecipientValidateAPI) {
    suspend fun judgeValidateRecipient(request: ValidateRequest): ApiState<ValidateResponse> {
        return apiCall { api.judgeValidate(request) }
    }
}

class ExecuteRepository @Inject constructor(private val api: ExecuteAPI) {
    suspend fun execute(request: ExecuteRequest): ApiState<ExecuteResponse> {
        return apiCall { api.execute(request) }
    }
}

class QuoteRepository @Inject constructor(private val api: QuoteAPI) {
    suspend fun quote(request: QuoteRequest): ApiState<QuoteResponse> {
        return apiCall { api.loadQuote(request) }
    }
}

class BalancesRepository @Inject constructor(private val api: BalancesAPI) {
    suspend fun balances(exchangeType: String): ApiState<BalancesResponse> {
        return apiCall { api.loadBalances(exchangeType) }
    }
}