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
        val testToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMTAxMjM0MTIzNCIsInR5cGUiOiJBQ0NFU1MiLCJpYXQiOjE3Njk2NjU4MjEsImV4cCI6MTc3NDkyNTIyMX0.nEZRJ9Laaug1Tes9-_CeHRjLOg9K9H2EBhLG1DAZjdX8rkpilC4EHaDB9Asm76PwPLUcUAne21QFwPPGsbfr4g"

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