package com.stable.scoi.presentation.ui.transfer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentTransferAmountBinding
import com.stable.scoi.presentation.ui.transfer.bottomsheet.AssetSymbolBottomSheet
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.transfer.bottomsheet.Network
import com.stable.scoi.presentation.ui.transfer.bottomsheet.NetworkBottomSheet
import com.stable.scoi.presentation.ui.transfer.bottomsheet.SendCheckBottomSheet
import com.stable.scoi.presentation.ui.transfer.bottomsheet.SetAssetSymbol
import com.stable.scoi.presentation.ui.transfer.bottomsheet.SetNetworkType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransferAmountFragment : SetNetworkType, BaseFragment<FragmentTransferAmountBinding, TransferState, TransferEvent, TransferViewModel>(
    FragmentTransferAmountBinding::inflate
) {
    override val viewModel: TransferViewModel by activityViewModels()

//    override fun onResume() {
//        super.onResume()
//        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
//    }

    override fun initView() {
        //input
//        binding.TransferAmountCoinTypeChangeIV.setOnClickListener {
//            AssetSymbolBottomSheet().show(
//                childFragmentManager,
//                "BottomSheet"
//            )
//        }

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

        viewModel.focusRemove(binding.TransferAmountET)

        binding.TransferNextTV.isEnabled = false

        binding.TransferAmountNetworkIV.setOnClickListener {
            binding.TransferAmountNetworkPopupLL.visibility = View.VISIBLE
            binding.TransferAmountNetworkPopupIV.visibility = View.VISIBLE
        }

        binding.root.setOnClickListener {
            binding.TransferAmountNetworkPopupLL.visibility = View.GONE
            binding.TransferAmountNetworkPopupIV.visibility = View.GONE
        }

        when (viewModel.assetSymbolType.value) {
            AssetSymbol.USDT -> {
                binding.TransferAmountCoinTypeTV.text = "USDT"
                binding.TransferAmountAvailableCoinTypeTV.text = "USDT"
                binding.TransferAmountNetworkAssetSymbolTV.text = "USDT"
            }
            AssetSymbol.USDC -> {
                binding.TransferAmountCoinTypeTV.text = "USDC"
                binding.TransferAmountAvailableCoinTypeTV.text = "USDC"
                binding.TransferAmountNetworkAssetSymbolTV.text = "USDC"
                binding.TransferAmountNetworkTypeTV.text = "이더리움"
                binding.TransferAmountNetworkFeeTV.text = "250"
                binding.TransferAmountNetworkSettingIV.isClickable = false
            }
            else -> Unit
        }

        binding.TransferAmountNetworkSettingIV.setOnClickListener {
            NetworkBottomSheet().show(
                childFragmentManager,
                "bottomsheet"
            )
        }



        //output
        binding.TransferAmountReceiverNameTV.text = viewModel.receiver.value.receiverKORName.toString()

        val address = viewModel.receiver.value.receiverAddress.toString()
        binding.TransferAmountReceiverAddressTV.text = viewModel.addressLineChange(address)

        when (viewModel.exchangeType.value) {
            Exchange.Upbit -> binding.TransferAmountReceieverExchangeIV.setImageResource(R.drawable.upbit_logo)
            Exchange.Bithumb -> binding.TransferAmountReceieverExchangeIV.setImageResource(R.drawable.bithumb_logo)
            else -> Unit
        }

        binding.apply {
            val rawInt = 0
            TransferAmountAmountPlus1BT.setOnClickListener {
                addButtonClicked(rawInt, 10000)
                TransferAmountET.requestFocus()
            }
            TransferAmountAmountPlus5BT.setOnClickListener {
                addButtonClicked(rawInt, 50000)
                TransferAmountET.requestFocus()
            }
            TransferAmountAmountPlus10BT.setOnClickListener {
                addButtonClicked(rawInt, 100000)
                TransferAmountET.requestFocus()
            }
            TransferAmountAmountPlusAllBT.setOnClickListener {
                //API 연동 후 추가 예정 (전체 금액)
                TransferAmountET.requestFocus()
            }
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

                    val rawInt = if (raw.isNotEmpty()) raw.toInt() else 0

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

                val amount = raw.toIntOrNull()

                if (amount == null) {
                    binding.TransferAmountWarningTV.visibility = View.GONE
                    binding.TransferNextTV.isEnabled = false
                } else if (amount <= 3) {
                    binding.TransferAmountWarningTV.visibility = View.VISIBLE
                    binding.TransferNextTV.isEnabled = false
                } else {
                    binding.TransferAmountWarningTV.visibility = View.GONE
                    binding.TransferNextTV.isEnabled = true
                }
            }

            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {}

            override fun onTextChanged(p0: CharSequence?,p1: Int,p2: Int,p3: Int) {}
        })

        repeatOnStarted(viewLifecycleOwner) {
//            launch {
//                viewModel.assetSymbolType.collect { assetSymbol ->
//                    when (assetSymbol) {
//                        AssetSymbol.USDT -> {
//                            binding.TransferAmountCoinTypeTV.text = "USDT"
//                            binding.TransferAmountAvailableCoinTypeTV.text = "USDT"
//                        }
//                        AssetSymbol.USDC -> {
//                            binding.TransferAmountCoinTypeTV.text = "USDC"
//                            binding.TransferAmountAvailableCoinTypeTV.text = "USDC"
//                        }
//                        else -> Unit
//                    }
//                }
//            }

            launch {
                viewModel.netWorkType.collect { network ->
                    when (network) {
                        Network.TRON -> {
                            binding.TransferAmountNetworkTypeTV.text = "트론"
                            binding.TransferAmountNetworkFeeTV.text = "FREE"
                        }
                        Network.ETHEREUM -> {
                            binding.TransferAmountNetworkTypeTV.text = "이더리움"
                            binding.TransferAmountNetworkFeeTV.text = "250"
                        }
                        Network.KAIA -> {
                            binding.TransferAmountNetworkTypeTV.text = "카이아"
                            binding.TransferAmountNetworkFeeTV.text = "250"
                        }
                        Network.APTOS -> {
                            binding.TransferAmountNetworkTypeTV.text = "앱토스"
                            binding.TransferAmountNetworkFeeTV.text = "250"
                        }
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

//    override fun typeUSDT() {
//        viewModel.setAssetSymbolUSDT()
//    }
//
//    override fun typeUSDC() {
//        viewModel.setAssetSymbolUSDC()
//    }

    override fun networkType(network: Network) {
        viewModel.submitNetwork(network)
    }

}