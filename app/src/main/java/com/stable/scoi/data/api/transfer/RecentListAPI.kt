package com.stable.scoi.data.api.transfer

import com.stable.scoi.data.base.ApiResponse
import com.stable.scoi.domain.model.transfer.Receiver
import com.stable.scoi.domain.model.transfer.RecentListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecentListAPI {
    @GET("/api/transfers/recipients/recent")
    suspend fun loadRecentList(
        @Query("limit") limit: Int,
        @Query("cursor") cursor: String
    ) : ApiResponse<RecentListResponse>
}