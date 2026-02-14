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
                        if (!state.isLoading) {
                            //title 거래소 표시
                            when (viewModel.exType) {
                                "UPBIT" -> {
                                    WalletDetailExchageIV.setImageResource(R.drawable.upbit_logo)
                                    WalletDetailExchageTV.text = "업비트"
                                }
                                "BITHUMB" -> {
                                    WalletDetailExchageIV.setImageResource(R.drawable.bithumb_logo)
                                    WalletDetailExchageTV.text = "빗썸"
                                }
                            }

                            //title 코인 종류
                            WalletDetailAssetSymbolTV.text =
                                when (state.transactionsTopupsDetailItem.market) {
                                    "KRW-USDT" -> "USDT"
                                    "KRW-USDC" -> "USDC"
                                    else -> ""
                                }

                            //title 거래 종류
                            WalletDetailWorkTypeTV.text =
                                when (state.transactionsTopupsDetailItem.side) {
                                    "bid" -> "충전"
                                    "ask" -> "현금 교환"
                                    else -> ""
                                }

                            //title 거래 상태
                            WalletDetailWorkResultTV.text =
                                when (state.transactionsTopupsDetailItem.state) {
                                    "done" -> "완료"
                                    "wait" -> "대기"
                                    "canceled" -> "취소"
                                    else -> ""
                                }

                            //title 금액
                            val amount = when (state.transactionsTopupsDetailItem.side) {
                                "bid" -> "+" + state.transactionsTopupsDetailItem.volume
                                "ask" -> "-" + state.transactionsTopupsDetailItem.volume
                                else -> ""
                            }
                            WalletDetailAmountTV.text = amount

                            //title 금액 코인 종류
                            WalletDetailAmountAssetSymbolTV.text =
                                when (state.transactionsTopupsDetailItem.market) {
                                    "KRW-USDT" -> "USDT"
                                    "KRW-USDC" -> "USDC"
                                    else -> ""
                                }

                            //title 금액 색 변화
                            when (state.transactionsTopupsDetailItem.state) {
                                "canceled" -> WalletDetailAmountTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                                else -> Unit
                            }


                            //거래 일시
                            WalletDetailTradeTimeTV.text =
                                formatDate(state.transactionsTopupsDetailItem.createAt)

                            //거래 상태
                            WalletDetailTradeStateTV.text =
                                when (state.transactionsTopupsDetailItem.state) {
                                    "done" -> "완료"
                                    "wait" -> "대기"
                                    "canceled" -> "취소"
                                    else -> ""
                                }

                            //거래 방식 -> API 명세서 수정 필요
                            WalletDetailTradeWayTV.text =
                                    when (state.transactionsTopupsDetailItem.ordType) {
                                        "limit" -> ""
                                        else -> ""
                                    }

                            //체결 가격
                            WalletDetailConcludePriceTV.text =
                                viewModel.addComma(state.transactionsTopupsDetailItem.price)

                            //체결 수량
                            WalletDetailConcludeAmountTV.text =
                                state.transactionsTopupsDetailItem.volume
                            WalletDetailConcludeAmountAssetSymbolTV.text =
                                when (state.transactionsTopupsDetailItem.market) {
                                    "KRW-USDT" -> "USDT"
                                    "KRW-USDC" -> "USDC"
                                    else -> ""
                                }

                            //체결 금액
                            WalletDetailConcludeTotalPriceTV.text =
                                viewModel.addComma(state.transactionsTopupsDetailItem.trades.funds)

                            //수수료
                            WalletDetailChargeTV.text = state.transactionsTopupsDetailItem.paidFee

                            //총 결제 금액
                            val totalAmount = state.transactionsTopupsDetailItem.trades.funds.toInt() + state.transactionsTopupsDetailItem.paidFee.toInt()
                            WalletDetailTotalTV.text = viewModel.addComma(totalAmount.toString())
                        }
                        else Unit
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
