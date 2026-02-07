package com.stable.scoi.domain.repository.transfer

import com.stable.scoi.data.api.transfer.RecentListAPI
import com.stable.scoi.data.base.ApiState
import com.stable.scoi.data.dto.response.Response
import com.stable.scoi.domain.model.transfer.RecentListRequest
import com.stable.scoi.domain.model.transfer.RecentListResponse
import javax.inject.Inject

class TransferRepository @Inject constructor(private val api: RecentListAPI) {
//    suspend fun loadRecentList(limit: Int, cursor: String): ApiState<RecentListResponse> {
//        return api.loadRecentList(limit,cursor).
//    }
}