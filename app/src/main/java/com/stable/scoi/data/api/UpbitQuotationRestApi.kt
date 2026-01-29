package com.stable.scoi.data.api

import com.stable.scoi.data.dto.response.UpbitMinuteCandleDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UpbitQuotationRestApi {

    @GET("v1/candles/minutes/{unit}")
    suspend fun getMinuteCandles(
        @Path("unit") unit: Int,
        @Query("market") market: String,
        @Query(/* value = */ "count") count: Int = 200,
    ): List<UpbitMinuteCandleDto>
}