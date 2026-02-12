package com.stable.scoi.data.api

import com.stable.scoi.data.base.ApiResponse
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface MyPageApi {
    // 내 정보 조회
    @GET("api/members/me")
    suspend fun getMyInfo(): ApiResponse<UserResponse>

    // 간편 비밀번호 변경
    @PATCH("api/members/me/password")
    suspend fun changePassword(@Body request: PasswordChangeRequest): ApiResponse<Unit>

    // 간편 비밀번호 재설정
    @POST("api/members/me/password/reset")
    suspend fun resetPassword(@Body request: PasswordResetRequest): ApiResponse<Unit>

    // 거래소 목록 조회
    @GET("api/exchanges")
    suspend fun getExchangeList(): ApiResponse<List<ExchangeInfo>>

    // API 키 목록 조회
    @GET("api/members/me/api-keys")
    suspend fun getApiKeys(): ApiResponse<List<ApiKeyInfo>>

    // API 키 등록 및 수정
    @POST("api/members/me/api-keys")
    suspend fun registerApiKeys(@Body request: List<ApiKeyRequest>): ApiResponse<List<String>>

    // API 키 삭제
    @DELETE("api/members/me/api-keys")
    suspend fun deleteApiKey(@Body request: ApiKeyDeleteRequest): ApiResponse<Unit>

    // FCM 토큰 등록
    @POST("api/members/me/fcm")
    suspend fun registerFcmToken(@Body request: FcmTokenRequest): ApiResponse<Unit>
}

@Serializable
data class UserResponse(
    val memberId: Int = 0,
    val koreanName: String = "",
    val englishName: String = "",
    val residentNumber: String = "",
    val phoneNumber: String = "",
    val memberType: String = "",
    val profileImageUrl: String? = null,
    val isBioRegistered: Boolean = false,
    val createdAt: String = ""
)

@Serializable
data class PasswordChangeRequest(
    val oldPassword: String,
    val newPassword: String
)

@Serializable
data class PasswordResetRequest(
    val phoneNumber: String,
    val newPassword: String
)

@Serializable
data class ExchangeInfo(
    val exchangeType: String,
    val isLinked: Boolean
)

@Serializable
data class ApiKeyListResult(
    val apiKeys: List<ApiKeyInfo>
)

@Serializable
data class ApiKeyInfo(
    val exchangeType: String,
    val publicKey: String,
    val secretKey: String
)

@Serializable
data class ApiKeyRequest(
    val exchangeType: String,
    val publicKey: String?,
    val secretKey: String?
)

@Serializable
data class ApiKeyDeleteRequest(
    val exchangeType: String
)

@Serializable
data class FcmTokenRequest(
    val token: String
)