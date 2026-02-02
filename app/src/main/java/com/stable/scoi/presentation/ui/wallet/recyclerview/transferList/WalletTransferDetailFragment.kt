package com.stable.scoi.presentation.ui.wallet.recyclerview.transferList

import androidx.fragment.app.activityViewModels
import com.stable.scoi.databinding.FragmentWalletTransferDetailBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.wallet.WalletEvent
import com.stable.scoi.presentation.ui.wallet.WalletState
import com.stable.scoi.presentation.ui.wallet.WalletViewModel

class WalletTransferDetailFragment: BaseFragment<FragmentWalletTransferDetailBinding, WalletState, WalletEvent, WalletViewModel>(
    FragmentWalletTransferDetailBinding::inflate
){
    override val viewModel: WalletViewModel by activityViewModels()

        override fun initView() {
            binding.apply {
                //detailFragment도 하나 더 만들어야 할듯 - 유지보수 측면에서 더 나을 것
            }
        }
}