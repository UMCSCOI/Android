package com.stable.scoi.domain.repository.auth

import com.stable.scoi.data.api.AuthApi
import com.stable.scoi.data.api.PinLoginRequest
import com.stable.scoi.data.api.PinLoginResponse
import com.stable.scoi.data.api.SmsRequest
import com.stable.scoi.data.api.SmsResponse
import com.stable.scoi.data.api.SmsVerifyRequest
import com.stable.scoi.data.api.SmsVerifyResponse
import com.stable.scoi.data.api.TokenRetryRequest
import com.stable.scoi.data.api.TokenRetryResponse
import com.stable.scoi.data.api.auth.SignUpApi
import com.stable.scoi.data.api.auth.SignUpRequest
import com.stable.scoi.data.api.auth.SignUpResponse
import com.stable.scoi.data.local.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val signUpApi: SignUpApi,
    private val preferenceManager: PreferenceManager
) {

    // SMS 발송 요청
    suspend fun sendSms(phoneNumber: String): Result<SmsResponse> {
        return try {
            val request = SmsRequest(phoneNumber = phoneNumber)
            val response = authApi.sendSmsNumber(request)

            if (response.isSuccess && response.result != null) {
                preferenceManager.saveSmsExpiredAt(response.result.expiredAt)
                Result.success(response.result)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //  SMS 인증 번호 검증
    suspend fun verifySms(phoneNumber: String, verifyCode: String): Result<SmsVerifyResponse> {
        return try {
            val request = SmsVerifyRequest(
                phoneNumber = phoneNumber,
                verificationCode = verifyCode
            )
            val response = authApi.verifySmsNumber(request)

            if (response.isSuccess && response.result != null) {
                Result.success(response.result)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 회원가입
    suspend fun signUp(request: SignUpRequest): Result<SignUpResponse> {
        return try {
            val response = signUpApi.signUp(request)

            if (response.isSuccess && response.result != null) {
                Result.success(response.result)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // PIN 로그인
    suspend fun pinLogin(
        phoneNumber: String,
        simplePassword: String
    ): Result<PinLoginResponse> {
        return try {
            val request = PinLoginRequest(
                phoneNumber = phoneNumber,
                simplePassword = simplePassword
            )
            val response = authApi.pinLogin(request)

            if (response.isSuccess && response.result != null) {
                preferenceManager.saveAccessToken(response.result.accessToken)
                preferenceManager.saveRefreshToken(response.result.refreshToken)
                Result.success(response.result)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun tokenRetry(
        refreshToken: String
    ): Result<TokenRetryResponse> {
        return try {
            val request = TokenRetryRequest(
                refreshToken = refreshToken
            )

            val response = authApi.tokenRetry(request)

            if (response.isSuccess && response.result != null) {
                preferenceManager.saveAccessToken(response.result.accessToken)
                preferenceManager.saveRefreshToken(response.result.refreshToken)

                Result.success(response.result)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun logout(): Result<Unit> {
        return try {
            val response = authApi.logout()

            if (response.isSuccess) {
                preferenceManager.clear()
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}