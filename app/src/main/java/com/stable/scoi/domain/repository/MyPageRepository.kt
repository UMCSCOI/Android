package com.stable.scoi.domain.repository

import com.stable.scoi.data.api.*
import com.stable.scoi.data.base.ApiState
import com.stable.scoi.data.base.apiCall
import javax.inject.Inject

class MyPageRepository @Inject constructor(
    private val api: MyPageApi
) {
    suspend fun getMyInfo(): ApiState<UserResponse> {
        return apiCall { api.getMyInfo() }
    }

    suspend fun changePassword(req: PasswordChangeRequest): ApiState<Unit> {
        return apiCall { api.changePassword(req) }
    }

    suspend fun resetPassword(req: PasswordResetRequest): ApiState<Unit> {
        return apiCall { api.resetPassword(req) }
    }

    suspend fun getExchangeList(): ApiState<List<ExchangeInfo>> {
        return apiCall { api.getExchangeList() }
    }

    suspend fun getApiKeys(): ApiState<ApiKeyListResult> {
        return apiCall { api.getApiKeys() }
    }

    suspend fun registerApiKeys(req: List<ApiKeyRequest>): ApiState<Unit> {
        return apiCall { api.registerApiKeys(req) }
    }

    suspend fun deleteApiKey(exchangeType: String): ApiState<Unit> {
        return apiCall { api.deleteApiKey(ApiKeyDeleteRequest(exchangeType)) }
    }

    suspend fun registerFcmToken(token: String): ApiState<Unit> {
        return apiCall { api.registerFcmToken(FcmTokenRequest(token)) }
    }
}