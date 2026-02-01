package com.stable.scoi.presentation.ui.wallet

import androidx.fragment.app.activityViewModels
import com.stable.scoi.databinding.FragmentWalletDetailBinding
import com.stable.scoi.presentation.base.BaseFragment

class WalletDetailFragment: BaseFragment<FragmentWalletDetailBinding, WalletState, WalletEvent, WalletViewModel>(
    FragmentWalletDetailBinding::inflate
){
    override val viewModel: WalletViewModel by activityViewModels()

        override fun initView() {
            binding.apply {

            }
        }
}