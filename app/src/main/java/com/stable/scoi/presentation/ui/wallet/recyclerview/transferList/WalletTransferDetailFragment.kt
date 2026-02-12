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
                        if (state.isLoading == false) {
                            WalletDetailTradeTimeTV.text = formatDate(state.transactionsRemitDetailItem.createdAt)
                            WalletDetailTradeStateTV.text = when (state.transactionsRemitDetailItem.state) {
                                "DONE" -> "완료"
                                else -> ""
                            }
                            WalletDetailTransferNetworkTV.text = when (state.transactionsRemitDetailItem.netType) {
                                "TRX" -> "트론"
                                "KAIA" -> "카이아"
                                "ETH" -> "이더리움"
                                "APT" -> "앱토스"
                                else -> ""
                            }
                            val amount = when (state.transactionsRemitDetailItem.type) {
                                "deposit" -> "+" + state.transactionsTopupsDetailItem.volume
                                "withdraw" -> "-" + state.transactionsTopupsDetailItem.volume
                                else -> ""
                            }
                            WalletDetailAmountTV.text = amount
                            WalletDetailTransferAmountTV.text = state.transactionsRemitDetailItem.amount
                            WalletDetailAmountAssetSymbolTV.text = state.transactionsRemitDetailItem.currency
                            WalletDetailChargeTV.text = state.transactionsRemitDetailItem.fee
                            WalletDetailConcludeFeeAssetSymbolTV.text = state.transactionsRemitDetailItem.currency
                            WalletDetailWorkTypeTV.text = when (state.transactionsRemitDetailItem.type) {
                                "withdraw" -> "출금"
                                "deposit" -> "입금"
                                else -> ""
                            }
                            WalletDetailAssetSymbolTV.text = state.transactionsRemitDetailItem.currency

                            when (state.transactionsRemitDetailItem.type) {
                                "deposit" -> WalletDetailAmountTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.active))
                                "withdraw" -> WalletDetailAmountTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
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
