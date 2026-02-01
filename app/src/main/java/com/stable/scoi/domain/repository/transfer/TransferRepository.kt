package com.stable.scoi.domain.repository.transfer

import com.stable.scoi.data.api.transfer.RecentListAPI
import com.stable.scoi.domain.model.transfer.RecentListRequest
import javax.inject.Inject

class TransferRepository @Inject constructor(private val api: RecentListAPI) {
    suspend fun loadRecentList(request: RecentListRequest) {
        //return api.loadRecentList(request).
    }
}