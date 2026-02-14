package com.stable.scoi.presentation.ui.wallet.recyclerview.transferList

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentWalletTransferDetailBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.wallet.WalletEvent
import com.stable.scoi.presentation.ui.wallet.WalletState
import com.stable.scoi.presentation.ui.wallet.WalletViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class WalletTransferDetailFragment: BaseFragment<FragmentWalletTransferDetailBinding, WalletState, WalletEvent, WalletViewModel>(
    FragmentWalletTransferDetailBinding::inflate
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

                            //title 코인 종류
                            WalletDetailAssetSymbolTV.text = state.transactionsRemitDetailItem.currency

                            //title 거래 종류
                            WalletDetailWorkTypeTV.text = when (state.transactionsRemitDetailItem.type) {
                                "withdraw" -> "출금"
                                "deposit" -> "입금"
                                else -> ""
                            }

                            //title 금액
                            val amount = when (state.transactionsRemitDetailItem.type) {
                                "deposit" -> "+" + state.transactionsTopupsDetailItem.volume
                                "withdraw" -> "-" + state.transactionsTopupsDetailItem.volume
                                else -> ""
                            }
                            WalletDetailAmountTV.text = amount

                            //title 금액 코인 종류
                            WalletDetailAmountAssetSymbolTV.text = state.transactionsRemitDetailItem.currency

                            //title 금액 색 변화
                            when (state.transactionsRemitDetailItem.type) {
                                "deposit" -> WalletDetailAmountTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.active))
                                "withdraw" -> WalletDetailAmountTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                                else -> Unit
                            }


                            //거래 일시
                            WalletDetailTradeTimeTV.text = formatDate(state.transactionsRemitDetailItem.createdAt)

                            //거래 상태
                            WalletDetailTradeStateTV.text = when (state.transactionsRemitDetailItem.state) {
                                "DONE" -> "완료"
                                else -> ""
                            }

                            //이체 네트워크 -> API 명세서 수정 필요
                            WalletDetailTransferNetworkTV.text = when (state.transactionsRemitDetailItem.netType) {
                                "TRX" -> "트론"
                                "KAIA" -> "카이아"
                                "ETH" -> "이더리움"
                                "APT" -> "앱토스"
                                else -> ""
                            }

                            //이체 수량
                            WalletDetailTransferAmountTV.text = state.transactionsRemitDetailItem.amount

                            //이체 수량 코인 종류
                            WalletDetailConcludeAmountAssetSymbolTV.text = state.transactionsRemitDetailItem.currency

                            //수수료
                            WalletDetailChargeTV.text = state.transactionsRemitDetailItem.fee

                            //수수료 코인 종류
                            WalletDetailConcludeFeeAssetSymbolTV.text = state.transactionsRemitDetailItem.currency


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
