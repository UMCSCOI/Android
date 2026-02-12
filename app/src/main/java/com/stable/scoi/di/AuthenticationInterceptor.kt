package com.stable.scoi.di

import android.util.Log
import com.stable.scoi.data.local.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

// TODO 토큰 관련 Repository 필요
@Singleton
class AuthenticationInterceptor
@Inject
constructor(
    private val preferenceManager: PreferenceManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken=preferenceManager.getAccessToken()?:""

        val requestBuilder = chain.request().newBuilder()

        if (accessToken.isNotEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $accessToken")
            Log.d("RETROFIT", "인증 헤더 추가됨: Bearer ${accessToken.take(10)}...") // 로그에 토큰 앞부분만 살짝 출력
        }
        val request = requestBuilder.build()

        Log.d(
            "RETROFIT",
            "AuthenticationInterceptor - intercept() called / request header: ${request.headers}",
        )
        return chain.proceed(request)
    }
}
