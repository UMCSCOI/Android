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
        val testToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMTAxMjM0MTIzNCIsInR5cGUiOiJBQ0NFU1MiLCJpYXQiOjE3NzA4ODIzMDAsImV4cCI6MTk1MDg4MjMwMH0.CsEcp-d4yPpSDsU4x0KIbnj3u_p-bV7SMWnasCFBovCZ5-FdsGawDk8741ZfahNVOXMkbxLleheDNWOOl94NIQ"

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
