package com.stable.scoi.data.api

import com.stable.scoi.data.base.ApiResponse
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/sms/send")
    suspend fun sendSmsNumber(
        @Body request: SmsRequest
    ): ApiResponse<SmsResponse>

    @POST("/auth/sms/verify")
    suspend fun verifySmsNumber(
        @Body request: SmsVerifyRequest
    ): ApiResponse<SmsVerifyResponse>



    @POST("/auth/login")
    suspend fun pinLogin(
        @Body request: PinLoginRequest
    ): ApiResponse<PinLoginResponse>

    @POST("/auth/reissue")
    suspend fun tokenRetry(
        @Body request: TokenRetryRequest
    ): ApiResponse<TokenRetryResponse>

    @POST("/auth/logout")
    suspend fun logout(): ApiResponse<Unit>
}



//request
@Serializable
data class SmsRequest(
    val phoneNumber: String = ""
)

@Serializable
data class SmsVerifyRequest(
    val phoneNumber: String = "",
    val verificationCode: String = ""
)



@Serializable
data class PinLoginRequest(
    val phoneNumber: String = "",
    val simplePassword: String = "",
    val keepLoggedIn: Boolean = false
)

@Serializable
data class TokenRetryRequest(
    val refreshToken: String = ""
)

//response
@Serializable
data class SmsResponse(
    val expiredAt: String = ""
)

@Serializable
data class SmsVerifyResponse(
    val verificationToken: String = ""
)



@Serializable
data class PinLoginResponse(
    val accessToken:String="",
    val refreshToken:String="",
    val accessTokenExpiresIn:Long=-1,
    val loginFailCount:Int=-1,
    val remainingAttempts:Int=-1
)

@Serializable
data class TokenRetryResponse(
    val accessToken: String = "",
    val refreshToken: String = "",
    val accessTokenExpiresIn: Long = -1
)