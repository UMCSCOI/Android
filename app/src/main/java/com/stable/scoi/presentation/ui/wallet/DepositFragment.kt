package com.stable.scoi.presentation.ui.wallet

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentWalletDepositBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.transfer.Exchange

class DepositFragment: BaseFragment<FragmentWalletDepositBinding, WalletState, WalletEvent, WalletViewModel>(
    FragmentWalletDepositBinding::inflate
) {
    override val viewModel: WalletViewModel by activityViewModels()

    override fun initView() {

        when (viewModel.exchangeType.value) {
            Exchange.Upbit -> {
                binding.WalletDepositReceieverExchangeIV.setImageResource(R.drawable.upbit_logo)
                binding.WalletDepositReceiverNameTV.text = "업비트"
            }
            Exchange.Bithumb -> {
                binding.WalletDepositReceieverExchangeIV.setImageResource(R.drawable.bithumb_logo)
                binding.WalletDepositReceiverNameTV.text = "빗썸"
            }
            Exchange.Binance -> {
                binding.WalletDepositReceieverExchangeIV.setImageResource(R.drawable.binance_logo)
                binding.WalletDepositReceiverNameTV.text = "BINANCE"
            }
            else -> Unit
        }

        binding.WalletDepositBackArrowIV.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.focusRemove(binding.WalletDepositAmountET)

        binding.apply {
            val rawInt = 0
            WalletDepositAmountPlus1BT.setOnClickListener {
                addButtonClicked(rawInt, 10000)
                WalletDepositAmountET.requestFocus()
            }
            WalletDepositAmountPlus5BT.setOnClickListener {
                addButtonClicked(rawInt, 50000)
                WalletDepositAmountET.requestFocus()
            }
            WalletDepositAmountPlus10BT.setOnClickListener {
                addButtonClicked(rawInt, 100000)
                WalletDepositAmountET.requestFocus()
            }
            WalletDepositAmountPlusAllBT.setOnClickListener {
                //API 연동 후 추가 예정 (전체 금액)
                WalletDepositAmountET.requestFocus()
            }
            WalletDepositAmountET.setText(rawInt.toString())
        }


        binding.WalletDepositAmountET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val raw = p0.toString().replace(",", "")

                binding.WalletDepositAmountET.removeTextChangedListener(this)

                if (raw.isNotEmpty()) {
                    val formatted = viewModel.addComma(raw)
                    binding.WalletDepositAmountET.setText(formatted)
                    binding.WalletDepositAmountET.setSelection(formatted.length)
                }

                binding.apply {
                    val rawInt = raw.toInt()
                    WalletDepositAmountPlus1BT.setOnClickListener {
                        addButtonClicked(rawInt, 10000)
                    }
                    WalletDepositAmountPlus5BT.setOnClickListener {
                        addButtonClicked(rawInt, 50000)
                    }
                    WalletDepositAmountPlus10BT.setOnClickListener {
                        addButtonClicked(rawInt, 100000)
                    }
                    WalletDepositAmountPlusAllBT.setOnClickListener {
                        //API 연동 후 추가 예정 (전체 금액)
                    }
                }

                binding.WalletDepositAmountET.addTextChangedListener(this)
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
    }

    private fun addButtonClicked(rawInt: Int, plus: Int) {
        val added = (rawInt + plus).toString()
        binding.WalletDepositAmountET.setText(added)
        binding.WalletDepositAmountET.setSelection(added.length+1)
    }
}