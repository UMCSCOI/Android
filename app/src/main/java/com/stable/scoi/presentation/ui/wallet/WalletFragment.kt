package com.stable.scoi.presentation.ui.wallet

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentWalletBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.transfer.Exchange
import com.stable.scoi.presentation.ui.wallet.bottomsheet.ExchangeBottomSheet
import com.stable.scoi.presentation.ui.wallet.bottomsheet.SetExchangeType
import com.stable.scoi.presentation.ui.wallet.recyclerview.chargeList.RecentChargeList
import com.stable.scoi.presentation.ui.wallet.recyclerview.chargeList.RecentChargeListOnClickListener
import com.stable.scoi.presentation.ui.wallet.recyclerview.chargeList.RecentChargeListRVAdapter
import com.stable.scoi.presentation.ui.wallet.recyclerview.transferList.RecentTransferList
import com.stable.scoi.presentation.ui.wallet.recyclerview.transferList.RecentTransferListOnClickListener
import com.stable.scoi.presentation.ui.wallet.recyclerview.transferList.RecentTransferListRVAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WalletFragment: RecentChargeListOnClickListener, RecentTransferListOnClickListener, SetExchangeType, BaseFragment<FragmentWalletBinding, WalletState, WalletEvent, WalletViewModel>(
    FragmentWalletBinding::inflate
) {
    private var recentTransferListData = ArrayList<RecentTransferList>()
    private var recentChargeListData = ArrayList<RecentChargeList>()

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

        val recentTransferListAdapter = RecentTransferListRVAdapter(recentTransferListData,this)
        val recentChargeListAdapter = RecentChargeListRVAdapter(recentChargeListData, this)

        binding.apply {
            WalletRecentListVP.adapter = recentTransferListAdapter
            WalletRecentListVP.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

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
                WalletRecentListVP.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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
                WalletRecentListVP.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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

    override fun RTLOnClickListener(recentTransferData: RecentTransferList) {
        viewModel.submitTransferDetails(recentTransferData)
        findNavController().navigate(R.id.wallet_transfer_detail_fragment)
    }

    override fun RCLOnClickListener(recentChargeData: RecentChargeList) {
        viewModel.submitChargeDetails(recentChargeData)
        findNavController().navigate(R.id.wallet_charge_detail_fragment)
    }
}