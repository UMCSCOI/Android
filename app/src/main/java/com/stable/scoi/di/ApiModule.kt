package com.stable.scoi.di

import com.stable.scoi.data.api.OkHttpUpbitCandleWsApi
import com.stable.scoi.data.api.UpbitQuotationRestApi
import com.stable.scoi.data.api.transfer.BalancesAPI
import com.stable.scoi.data.api.transfer.CancelOrderAPI
import com.stable.scoi.data.api.transfer.DirectoryListAPI
import com.stable.scoi.data.api.transfer.ExecuteAPI
import com.stable.scoi.data.api.transfer.QuoteAPI
import com.stable.scoi.data.api.transfer.RecipientValidateAPI
import com.stable.scoi.data.api.transfer.TransactionsDetailAPI
import com.stable.scoi.data.api.transfer.TransactionsRemitAPI
import com.stable.scoi.data.api.transfer.TransactionsTopupsAPI
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
    fun provideDirectoryListAPI(@AuthRetrofit retrofit: Retrofit): DirectoryListAPI {
        return retrofit.create(DirectoryListAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideValidateAPI(@AuthRetrofit retrofit: Retrofit): RecipientValidateAPI {
        return retrofit.create(RecipientValidateAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideExecuteAPI(@AuthRetrofit retrofit: Retrofit): ExecuteAPI {
        return retrofit.create(ExecuteAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideTransactionRemit(@AuthRetrofit retrofit: Retrofit): TransactionsRemitAPI {
        return retrofit.create(TransactionsRemitAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideTransactionTopups(@AuthRetrofit retrofit: Retrofit): TransactionsTopupsAPI {
        return retrofit.create(TransactionsTopupsAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideTransactionDetail(@AuthRetrofit retrofit: Retrofit): TransactionsDetailAPI {
        return retrofit.create(TransactionsDetailAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideCancelOrder(@AuthRetrofit retrofit: Retrofit): CancelOrderAPI {
        return retrofit.create(CancelOrderAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideQuote(@AuthRetrofit retrofit: Retrofit): QuoteAPI {
        return retrofit.create(QuoteAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideBalances(@AuthRetrofit retrofit: Retrofit): BalancesAPI {
        return retrofit.create(BalancesAPI::class.java)
    }


    @Provides
    @Singleton
    fun provideUpbitPrivateWsApi(client: OkHttpClient): OkHttpUpbitCandleWsApi =
        OkHttpUpbitCandleWsApi(client)

}