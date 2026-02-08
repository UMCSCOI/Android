package com.stable.scoi.presentation.ui.wallet.withdraw

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentWalletWithdrawBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.transfer.Exchange
import com.stable.scoi.presentation.ui.wallet.WalletEvent
import com.stable.scoi.presentation.ui.wallet.WalletState
import com.stable.scoi.presentation.ui.wallet.WalletViewModel

class WithdrawFragment: BaseFragment<FragmentWalletWithdrawBinding, WalletState, WalletEvent, WalletViewModel>(
    FragmentWalletWithdrawBinding::inflate
) {
    override val viewModel: WalletViewModel by activityViewModels()

    override fun initView() {
        binding.WalletWithdrawNextTV.isEnabled = false

        when (viewModel.exchangeType.value) {
            Exchange.Upbit -> {
                binding.WalletWithdrawExchangeIV.setImageResource(R.drawable.upbit_logo)
                binding.WalletWithdrawExchangeTV.text = "업비트"
            }
            Exchange.Bithumb -> {
                binding.WalletWithdrawExchangeIV.setImageResource(R.drawable.bithumb_logo)
                binding.WalletWithdrawExchangeTV.text = "빗썸"
            }
            else -> Unit
        }

        binding.WalletWithdrawBackArrowIV.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.WalletWithdrawNextTV.setOnClickListener {
            viewModel.submitAmount(binding.WalletWithdrawAmountET.text.toString().replace(",","").toInt())
            findNavController().navigate(R.id.wallet_withdraw_complete_fragment)
        }

        viewModel.focusRemove(binding.WalletWithdrawAmountET)

        binding.apply {
            val rawInt = 0
            WalletWithdrawAmountPlus1BT.setOnClickListener {
                addButtonClicked(rawInt, 10000)
                WalletWithdrawAmountET.requestFocus()
            }
            WalletWithdrawAmountPlus5BT.setOnClickListener {
                addButtonClicked(rawInt, 50000)
                WalletWithdrawAmountET.requestFocus()
            }
            WalletWithdrawAmountPlus10BT.setOnClickListener {
                addButtonClicked(rawInt, 100000)
                WalletWithdrawAmountET.requestFocus()
            }
            WalletWithdrawAmountPlusAllBT.setOnClickListener {
                //API 연동 후 추가 예정 (전체 금액)
                WalletWithdrawAmountET.requestFocus()
            }
        }


        binding.WalletWithdrawAmountET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val raw = p0.toString().replace(",", "")

                binding.WalletWithdrawAmountET.removeTextChangedListener(this)

                if (raw.isNotEmpty()) {
                    val formatted = viewModel.addComma(raw)
                    binding.WalletWithdrawAmountET.setText(formatted)
                    binding.WalletWithdrawAmountET.setSelection(formatted.length)
                }

                binding.apply {
                    val rawInt = if (raw.isNotEmpty()) raw.toInt() else 0

                    WalletWithdrawAmountPlus1BT.setOnClickListener {
                        addButtonClicked(rawInt, 10000)
                    }
                    WalletWithdrawAmountPlus5BT.setOnClickListener {
                        addButtonClicked(rawInt, 50000)
                    }
                    WalletWithdrawAmountPlus10BT.setOnClickListener {
                        addButtonClicked(rawInt, 100000)
                    }
                    WalletWithdrawAmountPlusAllBT.setOnClickListener {
                        //API 연동 후 추가 예정 (전체 금액)
                    }
                }

                binding.WalletWithdrawAmountET.addTextChangedListener(this)

                val amount = raw.toIntOrNull()

                if (amount == null) {
                    binding.WalletWithdrawNextTV.isEnabled = false
                } else {
                    binding.WalletWithdrawNextTV.isEnabled = true
                }
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
        binding.WalletWithdrawAmountET.setText(added)
        binding.WalletWithdrawAmountET.setSelection(added.length+1)
    }
}