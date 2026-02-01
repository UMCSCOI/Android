package com.stable.scoi.di

import com.stable.scoi.data.api.transfer.RecentListAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideRecentListAPI(retrofit: Retrofit): RecentListAPI {
        return retrofit.create(RecentListAPI::class.java)
    }

}