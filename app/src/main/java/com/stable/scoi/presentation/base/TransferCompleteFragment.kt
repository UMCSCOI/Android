package com.stable.scoi.presentation.base

import androidx.fragment.app.activityViewModels
import com.stable.scoi.databinding.FragmentTransferCompleteBinding

class TransferCompleteFragment :BaseFragment<FragmentTransferCompleteBinding, TransferState, TransferEvent, TransferViewModel>(
    FragmentTransferCompleteBinding::inflate) {
    override val viewModel: TransferViewModel by activityViewModels()

    override fun initView() {
        binding.TransferCompleteReceiverNameTV.text = viewModel.receiver.value.receiverName
        binding.TransferCompleteAddressTV.text = viewModel.receiver.value.receiverAddress

        binding.TransferCompleteAmountTV.text = viewModel.information.value.amount
        binding.TransferCompleteExchangeTV.text = viewModel.information.value.exchangeType
        binding.TransferCompleteAssetSymbolTV.text = viewModel.information.value.assetSymbol
    }
}