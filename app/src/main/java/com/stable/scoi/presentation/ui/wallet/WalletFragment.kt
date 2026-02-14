package com.stable.scoi.presentation.ui.wallet

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentWalletBinding
import com.stable.scoi.domain.model.wallet.CancelOrderRequest
import com.stable.scoi.domain.model.wallet.Transactions
import com.stable.scoi.domain.model.wallet.TransactionsCharge
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.transfer.Exchange
import com.stable.scoi.presentation.ui.wallet.bottomsheet.ArraySettingChargeBottomSheet
import com.stable.scoi.presentation.ui.wallet.bottomsheet.ArraySettingTransferBottomSheet
import com.stable.scoi.presentation.ui.wallet.bottomsheet.ExchangeBottomSheet
import com.stable.scoi.presentation.ui.wallet.bottomsheet.SearchNameBottomSheet
import com.stable.scoi.presentation.ui.wallet.bottomsheet.SetArraySettingCharge
import com.stable.scoi.presentation.ui.wallet.bottomsheet.SetArraySettingTransfer
import com.stable.scoi.presentation.ui.wallet.bottomsheet.SetExchangeType
import com.stable.scoi.presentation.ui.wallet.dialog.ChargeCancelDialogFragment
import com.stable.scoi.presentation.ui.wallet.recyclerview.chargeList.Counterparty
import com.stable.scoi.presentation.ui.wallet.recyclerview.chargeList.RecentChargeList
import com.stable.scoi.presentation.ui.wallet.recyclerview.chargeList.RecentChargeListOnClickListener
import com.stable.scoi.presentation.ui.wallet.recyclerview.chargeList.RecentChargeListRVAdapter
import com.stable.scoi.presentation.ui.wallet.recyclerview.transferList.RecentTransferList
import com.stable.scoi.presentation.ui.wallet.recyclerview.transferList.RecentTransferListOnClickListener
import com.stable.scoi.presentation.ui.wallet.recyclerview.transferList.RecentTransferListRVAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class WalletFragment : SetArraySettingCharge, SetArraySettingTransfer,
    RecentChargeListOnClickListener, RecentTransferListOnClickListener, SetExchangeType,
    BaseFragment<FragmentWalletBinding, WalletState, WalletEvent, WalletViewModel>(
        FragmentWalletBinding::inflate
    ) {
    val recentTransferListAdapter = RecentTransferListRVAdapter(this)
    val recentChargeListAdapter = RecentChargeListRVAdapter(this)


    private var toggleState: ToggleState = ToggleState.Transfer

    override val viewModel: WalletViewModel by activityViewModels()

    override fun initView() {

        viewModel.setExchangeUpbit()//기본 거래소
        viewModel.balances(viewModel.exType)

        binding.imageMyPage.setOnClickListener {
            findNavController().navigate(R.id.myPageFragment)
        }

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.exchangeType.collect { exchange ->
                    viewModel.balances(viewModel.exType)
                    when (exchange) {
                        Exchange.Upbit -> {
                            binding.WalletExchangeIV.setImageResource(R.drawable.upbit_logo)
                            binding.WalletExchangeTV.text = "업비트"
                        }

                        Exchange.Bithumb -> {
                            binding.WalletExchangeIV.setImageResource(R.drawable.bithumb_logo)
                            binding.WalletExchangeTV.text = "빗썸"
                        }

                        else -> Unit
                    }
                }
            }

            launch {
                viewModel.uiState.collect { state ->
                    recentTransferListAdapter.updateItems(state.transactionsRemitItems)
                    recentChargeListAdapter.updateItems(state.transactionsTopupsItems)
                    val amount = state.balances.find {
                        it.currency == "KRW"
                    }

                    if (amount != null) {
                        binding.WalletAmountTV.text = amount.balance
                    }
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
                when (toggleState) {
                    ToggleState.Transfer -> {
                        val bottomSheet = ArraySettingTransferBottomSheet()

                        // 콜백 설정
                        bottomSheet.setCallback(object : SetArraySettingTransfer {
                            override fun arraySettingTransfer(
                                sortType: String,
                                categoryType: String,
                                periodType: String
                            ) {
                                binding.apply {
                                    WalletRecentArrayTV.text = when (sortType) {
                                        "desc" -> "최신순"
                                        "asc" -> "과거순"
                                        else -> "최신순"
                                    }

                                    WalletRecentLengthTV.text = when (periodType) {
                                        "TODAY" -> "오늘"
                                        "ONE_MONTH" -> "1개월"
                                        "THREE_MONTHS" -> "3개월"
                                        "SIX_MONTHS" -> "6개월"
                                        else -> "기간"
                                    }

                                    WalletRecentSearchTypeTV.text = when (categoryType) {
                                        "ALL" -> "전체"
                                        "DEPOSIT" -> "입금"
                                        "WITHDRAW" -> "출금"
                                        else -> "전체"
                                    }

                                    WalletRecentStateTV.visibility = View.GONE

                                    setToggleAction(categoryType, periodType, sortType, 20)

                                    // 참고: Wallet_recent_state_TV (완료 등)는
                                    // 현재 바텀시트에서 선택하는 값이 없으므로 업데이트하지 않습니다.
                                }

                                viewModel.transactionRemit(viewModel.exType,categoryType,periodType,sortType, 20)
                            }
                        })

                        bottomSheet.show(parentFragmentManager, "ArraySettingTransferBottomSheet")
                    }

                    ToggleState.charge -> {
                        val bottomSheet = ArraySettingChargeBottomSheet()

                        // 콜백 설정
                        bottomSheet.setCallback(object : SetArraySettingCharge {
                            override fun arraySettingCharge(
                                sortType: String,
                                categoryType: String,
                                statusType: String,
                                periodType: String
                            ) {
                                binding.apply {
                                    WalletRecentArrayTV.text = when (sortType) {
                                        "desc" -> "최신순"
                                        "asc" -> "과거순"
                                        else -> "최신순"
                                    }

                                    WalletRecentLengthTV.text = when (periodType) {
                                        "TODAY" -> "오늘"
                                        "ONE_MONTH" -> "1개월"
                                        "THREE_MONTHS" -> "3개월"
                                        "SIX_MONTHS" -> "6개월"
                                        else -> "기간"
                                    }

                                    WalletRecentSearchTypeTV.text = when (categoryType) {
                                        "ALL" -> "전체"
                                        "CHARGE" -> "입금"
                                        "CASH_EXCHANGE" -> "출금"
                                        else -> "전체"
                                    }

                                    WalletRecentStateTV.text = when (statusType) {
                                        "WAIT" -> "대기"
                                        "DONE" -> "완료"
                                        "CANCEL" -> "취소"
                                        else -> "완료"
                                    }

                                    viewModel.transactionTopups(viewModel.exType,categoryType,statusType,periodType,sortType, 20)
                                }
                            }
                        })
                        bottomSheet.show(parentFragmentManager,"bottomsheet")
                    }
                }
            }
        }
    }

    fun setToggleAction(
        categoryType: String = "ALL",
        period: String = "ONE_MONTH",
        order: String = "desc",
        limit: Int = 20
    ) {
        binding.apply {
            WalletRecentListVP.adapter = recentTransferListAdapter
            viewModel.transactionRemit(
                exchangeType = viewModel.exType,
                type = categoryType,
                period = period,
                order = order,
                limit = limit
            )
            WalletRecentListVP.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            WalletRecentToggle1TV.setOnClickListener {
                WalletRecentToggle1TV.apply {
                    setBackgroundResource(R.drawable.toggle_activated)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
                WalletRecentToggle2TV.apply {
                    setBackgroundResource(R.drawable.toggle_disabled)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                }
                WalletRecentListVP.adapter = recentTransferListAdapter
                WalletRecentListVP.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                toggleState = ToggleState.Transfer
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
                WalletRecentListVP.adapter = recentChargeListAdapter
                WalletRecentListVP.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                toggleState = ToggleState.charge
            }
        }
    }

    override fun upbit() {
        viewModel.setExchangeUpbit()
    }

    override fun bithumb() {
        viewModel.setExchangeBithumb()
    }

    override fun empty() {
        viewModel.setExchange()
    }

    override fun RTLOnClickListener(recentTransferData: Transactions) {
        viewModel.transactionsDetail(
            viewModel.exType,
            "REMIT",
            recentTransferData.type,
            recentTransferData.uuid,
            recentTransferData.currency
        )
        findNavController().navigate(R.id.wallet_transfer_detail_fragment)
    }

    override fun RCLOnClickListener(recentChargeData: TransactionsCharge) {
        val currency = when (recentChargeData.market) {
            "KRW-USDT" -> "USDT"
            "KRW-USDC" -> "USDC"
            else -> ""
        }
        viewModel.transactionsDetail(
            viewModel.exType,
            "TOPUP",
            null,
            recentChargeData.uuid,
            currency
        )
        findNavController().navigate(R.id.wallet_charge_detail_fragment)
    }

    override fun cancelOnclickListener(cancelData: TransactionsCharge) {
        viewModel.submitCancelOrder(
            viewModel.exType,
            cancelData.uuid,
            null
        )
        viewModel.submitCancelData(cancelData)
        ChargeCancelDialogFragment().show(
            childFragmentManager,
            "dialog"
        )
    }

    override fun arraySettingCharge(
        sortType: String,
        categoryType: String,
        statusType: String,
        periodType: String
    ) {
        viewModel.submitArraySettingCharge(sortType, categoryType, statusType, periodType)

        viewModel.transactionTopups(
            exchangeType = viewModel.exType,
            type = viewModel.arraySettingCharge.value.categoryType,
            state = viewModel.arraySettingCharge.value.statusType,
            period = viewModel.arraySettingCharge.value.periodType,
            order = viewModel.arraySettingCharge.value.sortType,
            limit = 20
        )
    }

    override fun arraySettingTransfer(
        sortType: String,
        categoryType: String,
        periodType: String
    ) {
        viewModel.submitArraySettingTransfer(sortType, categoryType, periodType)

        viewModel.transactionRemit(
            exchangeType = viewModel.exType,
            type = viewModel.arraySettingTransfer.value.categoryType,
            period = viewModel.arraySettingTransfer.value.periodType,
            order = viewModel.arraySettingTransfer.value.sortType,
            limit = 20
        )
    }

    enum class ToggleState {
        Transfer, charge
    }
}