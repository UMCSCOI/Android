package com.stable.scoi.domain.model.transfer

data class RecentListRequest(
    val limit: Int,
    val cursor: String
)
