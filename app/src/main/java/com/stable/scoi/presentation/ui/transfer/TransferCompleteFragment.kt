package com.stable.scoi.presentation.ui.transfer

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentTransferCompleteBinding
import com.stable.scoi.presentation.base.BaseFragment

class TransferCompleteFragment :
    BaseFragment<FragmentTransferCompleteBinding, TransferState, TransferEvent, TransferViewModel>(
    FragmentTransferCompleteBinding::inflate) {
    override val viewModel: TransferViewModel by activityViewModels()

    override fun initView() {
        binding.TransferCompleteReceiverNameTV.text = viewModel.receiver.value.recipientKoName
        binding.TransferCompleteAddressTV.text = viewModel.receiver.value.walletAddress

        binding.TransferCompleteAmountTV.text = viewModel.information.value.amount
        binding.TransferCompleteExchangeTV.text = viewModel.exchangeToString(viewModel.exchangeType.value)
        binding.TransferCompleteAssetSymbolTV.text = viewModel.receiver.value.coinType

        binding.TransferCompleteCheckTransferTV.setOnClickListener {
            findNavController().navigate(R.id.wallet_fragment)
        }
        binding.TransferCompleteCompleteTV.setOnClickListener {
            //홈으로 이동
        }
    }
}