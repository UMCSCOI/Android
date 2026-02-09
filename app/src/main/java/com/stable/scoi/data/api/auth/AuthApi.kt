package com.stable.scoi.data.api.auth

import com.stable.scoi.data.base.ApiResponse
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/sms/send")
    suspend fun sendSmsNumber(
        @Body request: SmsRequest
    ): ApiResponse<SmsResponse>
}

@Serializable
data class SmsRequest(
    val phoneNumber : String=""
)

@Serializable
data class SmsResponse(
    val expiredAt:String=""
)