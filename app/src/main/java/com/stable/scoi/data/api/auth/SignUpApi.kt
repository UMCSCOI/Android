package com.stable.scoi.data.api.auth


import com.stable.scoi.data.base.ApiResponse
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi{

    @POST("/auth/signup")
    suspend fun signUp(
        @Body request: SignUpRequest
    ):ApiResponse<SignUpResponse>

}

@Serializable
data class SignUpRequest(
    val phoneNumber: String = "",
    val koreanName: String = "",
    val englishName: String = "",
    val residentNumber: String = "",
    val simplePassword: String = "",
    val memberType: String = "",
    val verificationToken: String = "",
    val isBioRegistered: Boolean = false,
    val apiKeys: List<ApiKeyInfo> = emptyList()
)

@Serializable
data class ApiKeyInfo(
    val exchangeType: String="",
    val publicKey: String="",
    val secretKey: String=""
)

@Serializable
data class SignUpResponse(
    val memberId:Long=-1,
    val koreanName: String="",
    val simplePassword:String=""
)