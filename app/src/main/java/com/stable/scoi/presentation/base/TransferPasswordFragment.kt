package com.stable.scoi.presentation.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.stable.scoi.databinding.FragmentTransferBinding
import com.stable.scoi.databinding.FragmentTransferBinding.inflate
import com.stable.scoi.databinding.FragmentTransferPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class TransferPasswordFragment: BaseFragment<FragmentTransferPasswordBinding, TransferState, TransferEvent, TransferViewModel>(
    FragmentTransferPasswordBinding::inflate
) {
    override val viewModel: TransferViewModel by activityViewModels()

    override fun initView() {

    }
}