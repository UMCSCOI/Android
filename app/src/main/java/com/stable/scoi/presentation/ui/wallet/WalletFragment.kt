package com.stable.scoi.presentation.ui.wallet

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentWalletBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.transfer.Exchange
import com.stable.scoi.presentation.ui.wallet.bottomsheet.ExchangeBottomSheet
import com.stable.scoi.presentation.ui.wallet.bottomsheet.SetExchangeType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WalletFragment: SetExchangeType, BaseFragment<FragmentWalletBinding, WalletState, WalletEvent, WalletViewModel>(
    FragmentWalletBinding::inflate
) {
    override val viewModel: WalletViewModel by activityViewModels()

    override fun initView() {

        viewModel.setExchangeUpbit()//기본 거래소

        repeatOnStarted(viewLifecycleOwner) {
            viewModel.exchangeType.collect { exchange ->
                when (exchange) {
                    Exchange.Upbit -> {
                        binding.WalletExchangeIV.setImageResource(R.drawable.upbit_logo)
                        binding.WalletExchangeTV.text = "업비트"
                    }
                    Exchange.Bithumb -> {
                        binding.WalletExchangeIV.setImageResource(R.drawable.bithumb_logo)
                        binding.WalletExchangeTV.text = "빗썸"
                    }
                    Exchange.Binance -> {
                        binding.WalletExchangeIV.setImageResource(R.drawable.binance_logo)
                        binding.WalletExchangeTV.text = "BINANCE"
                    }
                    else -> Unit
                }
            }
        }

        binding.apply {
            WalletExchangeSelectLL.setOnClickListener {
                ExchangeBottomSheet().show(
                    childFragmentManager,
                    "Bottomsheet"
                )
            }

            WalletWithdrawBT.setOnClickListener {
                findNavController().navigate(R.id.wallet_withdraw_fragment)
            }

            WalletDepositBT.setOnClickListener {
                findNavController().navigate(R.id.wallet_deposit_fragment)
            }

            WalletRecentArraySettingIV.setOnClickListener {
                // 정렬 BottomSheet Navigate
            }

            WalletRecentSearchIV.setOnClickListener {
                // 검색 BottomSheet Navigate
            }

            setToggleAction()
        }


    }

    private fun setToggleAction() {
        binding.apply {
            WalletRecentToggle1TV.setOnClickListener {
                WalletRecentToggle1TV.apply {
                    setBackgroundResource(R.drawable.toggle_activated)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
                WalletRecentToggle2TV.apply {
                    setBackgroundResource(R.drawable.toggle_disabled)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                }
                //Adapter 추가
            }
            WalletRecentToggle2TV.setOnClickListener {
                WalletRecentToggle1TV.apply {
                    setBackgroundResource(R.drawable.toggle_disabled)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                }
                WalletRecentToggle2TV.apply {
                    setBackgroundResource(R.drawable.toggle_activated)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
                //Adapter 추가
            }
        }
    }

    override fun upbit() {
        viewModel.setExchangeUpbit()
    }

    override fun bithumb() {
        viewModel.setExchangeBithumb()
    }

    override fun binance() {
        viewModel.setExchangeBinance()
    }

    override fun empty() {
        viewModel.setExchange()
    }
}