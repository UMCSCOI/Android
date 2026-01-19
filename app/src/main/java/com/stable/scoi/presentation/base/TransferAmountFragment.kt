package com.stable.scoi.presentation.base

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentSendcheckBottomsheetBinding
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

        binding.TransferNextTV.setOnClickListener {
            viewModel.submitInformation(binding.TransferAmountET.text.toString())
            Log.d("information", viewModel.information.toString())
            viewModel.onSendCheckClicked()
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

        binding.TransferAmountET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val raw = p0.toString().replace(",", "")
                binding.TransferAmountET.removeTextChangedListener(this)
                if (raw.isNotEmpty()) {
                    val formatted = viewModel.addComma(raw)
                    binding.TransferAmountET.setText(formatted)
                    binding.TransferAmountET.setSelection(formatted.length)
                }

                binding.TransferAmountET.addTextChangedListener(this)
            }

            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {}

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {}
        })

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
                    binding.TransferAmountAvailableCoinTypeTV.text = "USDT"
                }
                AssetSymbol.USDC -> {
                    binding.TransferAmountCoinTypeTV.text = "USDC"
                    binding.TransferAmountAvailableCoinTypeTV.text = "USDC"
                }
                else -> Unit
            }
        }

        viewModel.sendCheckEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                TransferEvent.Submit -> {
                    SendCheckBottomSheet().show(
                        parentFragmentManager,
                        "bottomsheet"
                    )
                }
                else -> Unit
            }
        }


    }

}