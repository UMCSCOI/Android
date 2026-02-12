package com.stable.scoi.presentation.ui.transfer.recyclerview

import com.stable.scoi.domain.model.transfer.DirectoryResult
import com.stable.scoi.presentation.ui.transfer.Directory

interface DirectoryOnClickListener {
    fun dtOnclickListener(result: DirectoryResult)
}