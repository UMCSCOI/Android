package com.stable.scoi.di

import com.stable.scoi.data.api.OkHttpUpbitCandleWsApi
import com.stable.scoi.data.api.transfer.BalancesAPI
import com.stable.scoi.data.api.transfer.CancelOrderAPI
import com.stable.scoi.data.api.transfer.DirectoryListAPI
import com.stable.scoi.data.api.transfer.ExecuteAPI
import com.stable.scoi.data.api.transfer.QuoteAPI
import com.stable.scoi.data.api.transfer.RecipientValidateAPI
import com.stable.scoi.data.api.transfer.TransactionsDetailAPI
import com.stable.scoi.data.api.transfer.TransactionsRemitAPI
import com.stable.scoi.data.api.transfer.TransactionsTopupsAPI
import com.stable.scoi.data.api.ChargeApi
import com.stable.scoi.data.api.MyPageApi
import com.stable.scoi.data.api.AuthApi
import com.stable.scoi.data.api.auth.SignUpApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.jvm.java

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

    @Singleton
    @Provides
    fun provideChargeAPI(@AuthRetrofit retrofit: Retrofit): ChargeApi {
        return retrofit.create(ChargeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUpbitPrivateWsApi(client: OkHttpClient): OkHttpUpbitCandleWsApi =
        OkHttpUpbitCandleWsApi(client)

    @Provides
    @Singleton
    fun provideAuthApi(
        @NormalRetrofit retrofit: Retrofit
    ): AuthApi {
     return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSignUpApi(
        @NormalRetrofit retrofit: Retrofit
    ): SignUpApi{
        return retrofit.create(SignUpApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMyPageApi(@AuthRetrofit retrofit: Retrofit): MyPageApi {
        return retrofit.create(MyPageApi::class.java)
    }
}