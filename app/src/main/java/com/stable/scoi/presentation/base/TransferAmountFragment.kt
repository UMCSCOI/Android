package com.stable.scoi.presentation.base

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentTransferAmountBinding
import com.stable.scoi.databinding.FragmentTransferBinding
import com.stable.scoi.databinding.FragmentTransferBinding.inflate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferAmountFragment : BaseFragment<FragmentTransferAmountBinding, TransferState, TransferEvent, TransferViewModel>(
    FragmentTransferAmountBinding::inflate
) {
    override val viewModel: TransferViewModel by activityViewModels()

    override fun initView() {
        //input
        binding.TransferAmountCoinTypeChangeIV.setOnClickListener {
            viewModel.onAssetSymbolClicked()
        }


        //output
        binding.TransferAmountReceiverNameTV.text = viewModel.receiver.value.receiverName.toString()

        val address = viewModel.receiver.value.receiverAddress.toString()
        binding.TransferAmountReceiverAddressTV.text = viewModel.addressLineChange(address)

        when (viewModel.exchangeType.value) {
            Exchange.Upbit -> binding.TransferAmountReceieverExchangeIV.setImageResource(R.drawable.upbit_logo)
            Exchange.Bithumb -> binding.TransferAmountReceieverExchangeIV.setImageResource(R.drawable.bithumb_logo)
            Exchange.Binance -> binding.TransferAmountReceieverExchangeIV.setImageResource(R.drawable.binance_logo)
            else -> Unit
        }

        viewModel.assetSymbolEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                TransferEvent.Submit -> {
                    AssetSymbolBottomSheet().show(
                        parentFragmentManager,
                        "BottomSheet"
                    )
                }
                else -> Unit
            }
        }

        viewModel.assetSymbolType.observe(viewLifecycleOwner) { assetSymbol ->
            when (assetSymbol) {
                AssetSymbol.USDT -> {
                    binding.TransferAmountCoinTypeTV.text = "USDT"
                }
                AssetSymbol.USDC -> {
                    binding.TransferAmountCoinTypeTV.text = "USDC"
                }
                else -> Unit
            }
        }

    }

}