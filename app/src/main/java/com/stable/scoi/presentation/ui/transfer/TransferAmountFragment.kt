package com.stable.scoi.presentation.ui.transfer


import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentTransferAmountBinding
import com.stable.scoi.domain.model.transfer.QuoteRequest
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.transfer.bottomsheet.Network
import com.stable.scoi.presentation.ui.transfer.bottomsheet.NetworkBottomSheet
import com.stable.scoi.presentation.ui.transfer.bottomsheet.SendCheckBottomSheet
import com.stable.scoi.presentation.ui.transfer.bottomsheet.SetNetworkType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransferAmountFragment : SetNetworkType, BaseFragment<FragmentTransferAmountBinding, TransferState, TransferEvent, TransferViewModel>(
    FragmentTransferAmountBinding::inflate
) {
    override val viewModel: TransferViewModel by activityViewModels()

    override fun initView() {

        viewModel.balances(viewModel.myExchange.value)

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.collect { state ->
                    val myAssetAmount = state.balances.find { it.currency == "KRW" }
                    if (myAssetAmount != null )
                    binding.TransferAmountAvailableAmountTV.text = myAssetAmount.balance
                }
            }
        }
        //input
        binding.TransferNextTV.setOnClickListener {

            val currentState = viewModel.uiState.value

            val myAssetAmount = currentState.balances
                .find { it.currency == viewModel.myAssetSymbol.value }

            if (myAssetAmount == null) return@setOnClickListener

            val request = QuoteRequest(
                available = myAssetAmount.balance,
                amount = binding.TransferAmountET.text.toString().replace(",", ""),
                coinType = viewModel.myAssetSymbol.value,
                networkType = viewModel.formattedNetwork(viewModel.netWorkType.value),
                networkFee = binding.TransferAmountNetworkFeeTV.text.toString()
            )

            viewModel.quote(request)
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
                binding.TransferAmountNetworkFeeTV.text = "1"
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
        binding.TransferAmountReceiverNameTV.text = viewModel.receiver.value.recipientKoName

        val address = viewModel.receiver.value.walletAddress
        binding.TransferAmountReceiverAddressTV.text = viewModel.addressLineChange(address)

        val availableAmount = viewModel.uiState.value.validateBalance.availableAmount
        binding.TransferAmountAvailableAmountTV.text = viewModel.addComma(availableAmount)

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
                addButtonClicked(rawInt, availableAmount.toInt())
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
                        addButtonClicked(rawInt, availableAmount.toInt())
                    }
                }

                binding.TransferAmountET.addTextChangedListener(this)

                val amount = raw.toIntOrNull()

                if (amount == null) {
                    binding.TransferAmountWarningTV.visibility = View.GONE
                    binding.TransferNextTV.isEnabled = false
                } else if (amount <= 2) {
                    binding.TransferAmountWarningTV.visibility = View.VISIBLE
                    binding.TransferNextTV.isEnabled = false
                } else {
                    binding.TransferAmountWarningTV.visibility = View.GONE
                    binding.TransferNextTV.isEnabled = true
                }
            }

            override fun beforeTextChanged(p0: CharSequence?,p1: Int,p2: Int,p3: Int) {}
            override fun onTextChanged(p0: CharSequence?,p1: Int,p2: Int,p3: Int) {}
        })

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.netWorkType.collect { network ->
                    when (network) {
                        Network.TRON -> {
                            binding.TransferAmountNetworkTypeTV.text = "트론"
                            binding.TransferAmountNetworkFeeTV.text = "FREE"
                        }
                        Network.ETHEREUM -> {
                            binding.TransferAmountNetworkTypeTV.text = "이더리움"
                            binding.TransferAmountNetworkFeeTV.text = "1"
                        }
                        Network.KAIA -> {
                            binding.TransferAmountNetworkTypeTV.text = "카이아"
                            binding.TransferAmountNetworkFeeTV.text = "0.1"
                        }
                        Network.APTOS -> {
                            binding.TransferAmountNetworkTypeTV.text = "앱토스"
                            binding.TransferAmountNetworkFeeTV.text = "0.1"
                        }
                    }
                }
            }
            launch {
                viewModel.uiEvent.collect { event ->
                    when (event) {
                        TransferEvent.NavigateToNextPage -> {
                            SendCheckBottomSheet().show(
                                parentFragmentManager,
                                "bottomsheet"
                            )
                        }
                        is TransferEvent.ShowError -> {
                            binding.TransferAmountWarningTV.visibility = View.VISIBLE
                            binding.TransferAmountWarningTV.text = event.message
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

    override fun networkType(network: Network) {
        viewModel.submitNetwork(network)
    }

}