package com.stable.scoi.di

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

// TODO 토큰 관련 Repository 필요
@Singleton
class AuthenticationInterceptor
@Inject
constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // val accessToken = runBlocking { repository.getAccessToken().first() }
        val testToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMTAxMjM0MTIzNCIsInR5cGUiOiJBQ0NFU1MiLCJpYXQiOjE3NzA4ODkwODcsImV4cCI6MTk1MDg4OTA4N30.mlpIoIk95c0vvo0rtLpjaObQ5K0rFG6uR1cRTBxTykxmHA0Tr2D8BLhJPwjzGgragvqaol90Q1fblyjO_yjixg"

        val request =
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${testToken}").build()

        Log.d(
            "RETROFIT",
            "AuthenticationInterceptor - intercept() called / request header: ${request.headers}",
        )
        return chain.proceed(request)
    }
}
