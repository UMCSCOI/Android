package com.stable.scoi.presentation.ui.wallet.deposit

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentWalletDepositCompleteBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.wallet.WalletEvent
import com.stable.scoi.presentation.ui.wallet.WalletState
import com.stable.scoi.presentation.ui.wallet.WalletViewModel

class WalletDepositCompleteFragment: BaseFragment<FragmentWalletDepositCompleteBinding, WalletState, WalletEvent, WalletViewModel>(
    FragmentWalletDepositCompleteBinding::inflate
) {
    override val viewModel: WalletViewModel by activityViewModels()

    override fun initView() {
        binding.WalletCompleteCompleteTV.setOnClickListener {
            findNavController().navigate(R.id.wallet_fragment)
        }
    }
}