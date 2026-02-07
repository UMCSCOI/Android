package com.stable.scoi.di

import com.stable.scoi.data.api.transfer.RecentListAPI
import com.stable.scoi.data.api.OkHttpUpbitCandleWsApi
import com.stable.scoi.data.api.UpbitQuotationRestApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideRecentListAPI(retrofit: Retrofit): RecentListAPI {
        return retrofit.create(RecentListAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideUpbitPrivateWsApi(client: OkHttpClient): OkHttpUpbitCandleWsApi =
        OkHttpUpbitCandleWsApi(client)

}