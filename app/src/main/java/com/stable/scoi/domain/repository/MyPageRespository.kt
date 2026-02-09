package com.stable.scoi.domain.repository

import com.stable.scoi.data.api.MyPageApi
import com.stable.scoi.data.api.REsponse
import com.stable.scoi.data.base.ApiState
import com.stable.scoi.data.base.apiCall

class MyPageRespository(
    private val api: MyPageApi
) {
    suspend fun getMyInfo(): ApiState<REsponse> {
        apiCall { api.getMyInfo() }



    }
}