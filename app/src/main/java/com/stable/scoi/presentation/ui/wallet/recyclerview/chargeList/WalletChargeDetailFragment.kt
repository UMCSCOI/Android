package com.stable.scoi.presentation.ui.wallet.recyclerview.chargeList

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentWalletChargeDetailBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.wallet.WalletEvent
import com.stable.scoi.presentation.ui.wallet.WalletState
import com.stable.scoi.presentation.ui.wallet.WalletViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class WalletChargeDetailFragment: BaseFragment<FragmentWalletChargeDetailBinding, WalletState, WalletEvent, WalletViewModel>(
    FragmentWalletChargeDetailBinding::inflate
) {
    override val viewModel: WalletViewModel by activityViewModels()

    override fun initView() {
        binding.apply {
            WalletDetailBackArrowIV.setOnClickListener {
                findNavController().popBackStack()
            }

            repeatOnStarted(viewLifecycleOwner) {
                launch {
                    viewModel.uiState.collect { state ->
                        if (state.isLoading == false) {
                            WalletDetailTradeTimeTV.text =
                                formatDate(state.transactionsTopupsDetailItem.createAt)
                            WalletDetailTradeStateTV.text =
                                when (state.transactionsTopupsDetailItem.state) {
                                    "done" -> "완료"
                                    "wait" -> "대기"
                                    "canceled" -> "취소"
                                    else -> ""
                                }
                            WalletDetailChargeTV.text = state.transactionsTopupsDetailItem.paidFee
                            WalletDetailChargeAssetSymbolTV.text =
                                when (state.transactionsTopupsDetailItem.market) {
                                    "KRW-USDT" -> "USDT"
                                    "KRW-USDC" -> "USDC"
                                    else -> ""
                                }
                            val amount = when (state.transactionsTopupsDetailItem.side) {
                                "bid" -> "+" + state.transactionsTopupsDetailItem.volume
                                "ask" -> "-" + state.transactionsTopupsDetailItem.volume
                                else -> ""
                            }
                            WalletDetailAmountTV.text = amount
                            WalletDetailConcludeAmountTV.text =
                                state.transactionsTopupsDetailItem.volume
                            WalletDetailConcludePriceTV.text =
                                state.transactionsTopupsDetailItem.price
                            WalletDetailAmountAssetSymbolTV.text =
                                when (state.transactionsTopupsDetailItem.market) {
                                    "KRW-USDT" -> "USDT"
                                    "KRW-USDC" -> "USDC"
                                    else -> ""
                                }
                            WalletDetailWorkTypeTV.text =
                                when (state.transactionsTopupsDetailItem.side) {
                                    "bid" -> "충전"
                                    "ask" -> "현금 교환"
                                    else -> ""
                                }
                            WalletDetailWorkResultTV.text =
                                when (state.transactionsTopupsDetailItem.state) {
                                    "done" -> "완료"
                                    "wait" -> "대기"
                                    "canceled" -> "취소"
                                    else -> ""
                                }
                            WalletDetailAssetSymbolTV.text =
                                when (state.transactionsTopupsDetailItem.market) {
                                    "KRW-USDT" -> "USDT"
                                    "KRW-USDC" -> "USDC"
                                    else -> ""
                                }
                            WalletDetailConcludeTotalPriceTV.text =
                                state.transactionsTopupsDetailItem.trades.funds

                            when (state.transactionsTopupsDetailItem.state) {
                                "canceled" -> WalletDetailAmountTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                                else -> Unit
                            }
                        }
                    }
                }
            }
        }
    }

    fun formatDate(dateString: String): String {
        return try {
            val input = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ssXXX",
                Locale.getDefault()
            )
            val output = SimpleDateFormat(
                "MM.dd HH:mm:ss",
                Locale.getDefault()
            )
            val date = input.parse(dateString)
            output.format(date!!)
        } catch (e: Exception) {
            dateString
        }
    }
}
