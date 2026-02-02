package com.stable.scoi.presentation.ui.transfer

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentTransferAmountBinding
import com.stable.scoi.presentation.ui.transfer.bottomsheet.AssetSymbolBottomSheet
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.transfer.bottomsheet.SendCheckBottomSheet
import com.stable.scoi.presentation.ui.transfer.bottomsheet.SetAssetSymbol
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransferAmountFragment : SetAssetSymbol, BaseFragment<FragmentTransferAmountBinding, TransferState, TransferEvent, TransferViewModel>(
    FragmentTransferAmountBinding::inflate
) {
    override val viewModel: TransferViewModel by activityViewModels()



    override fun initView() {
        //input
        binding.TransferAmountCoinTypeChangeIV.setOnClickListener {
            AssetSymbolBottomSheet().show(
                childFragmentManager,
                "BottomSheet"
            )
        }

        binding.TransferNextTV.setOnClickListener {
            viewModel.submitInformation(binding.TransferAmountET.text.toString())

            SendCheckBottomSheet().show(
                parentFragmentManager,
                "bottomsheet"
            )
        }

        binding.TransferAmountBackArrowIV.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.setAssetSymbolUSDT() //기본 assetSymbol
        viewModel.focusRemove(binding.TransferAmountET)



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

                binding.apply {
                    val rawInt = raw.toInt()
                    TransferAmountAmountPlus1BT.setOnClickListener {
                        addButtonClicked(rawInt, 10000)
                    }
                    TransferAmountAmountPlus5BT.setOnClickListener {
                        addButtonClicked(rawInt, 50000)
                    }
                    TransferAmountAmountPlus10BT.setOnClickListener {
                        addButtonClicked(rawInt, 100000)
                    }
                    TransferAmountAmountPlusAllBT.setOnClickListener {
                        //API 연동 후 추가 예정 (전체 금액)
                    }
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

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.assetSymbolType.collect { assetSymbol ->
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
            }
        }
    }

    private fun addButtonClicked(rawInt: Int, plus: Int) {
        val added = (rawInt + plus).toString()
        binding.TransferAmountET.setText(added)
        binding.TransferAmountET.setSelection(added.length+1)
    }

    override fun typeUSDT() {
        viewModel.setAssetSymbolUSDT()
    }

    override fun typeUSDC() {
        viewModel.setAssetSymbolUSDC()
    }

}