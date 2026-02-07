package com.stable.scoi.presentation.ui.wallet.withdraw

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentWalletWithdrawCompleteBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.wallet.WalletEvent
import com.stable.scoi.presentation.ui.wallet.WalletState
import com.stable.scoi.presentation.ui.wallet.WalletViewModel

class WalletWithdrawCompleteFragment: BaseFragment<FragmentWalletWithdrawCompleteBinding, WalletState, WalletEvent, WalletViewModel>(
    FragmentWalletWithdrawCompleteBinding::inflate
) {
    override val viewModel: WalletViewModel by activityViewModels()

    override fun initView() {
        binding.WalletCompleteCompleteTV.setOnClickListener {
            findNavController().navigate(R.id.wallet_fragment)
        }
    }
}