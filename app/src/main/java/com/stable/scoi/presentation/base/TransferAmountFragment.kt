package com.stable.scoi.presentation.base

import androidx.fragment.app.viewModels
import com.stable.scoi.databinding.FragmentTransferAmountBinding
import com.stable.scoi.databinding.FragmentTransferBinding
import com.stable.scoi.databinding.FragmentTransferBinding.inflate

class TransferAmountFragment : BaseFragment<FragmentTransferAmountBinding, TransferState, TransferEvent, TransferViewModel>(
    FragmentTransferAmountBinding::inflate
) {
    override val viewModel: TransferViewModel by viewModels()

    override fun initView() {

    }

}