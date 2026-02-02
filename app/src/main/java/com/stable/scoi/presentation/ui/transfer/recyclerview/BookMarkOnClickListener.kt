package com.stable.scoi.presentation.ui.transfer.recyclerview

import com.stable.scoi.presentation.ui.transfer.BookMark
import com.stable.scoi.presentation.ui.transfer.Recent

interface BookMarkOnClickListener {
    fun bmOnclickListener(bookMark: BookMark)
}

interface RecentOnCliCKListener {
    fun rcOnclickListener(recent: Recent)
}