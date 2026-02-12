package com.stable.scoi.presentation.ui.charge.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentChargeMainBinding
import com.stable.scoi.domain.model.enums.AccountType
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.charge.bottomSheet.SelectAccountBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChargeMainFragment : BaseFragment<FragmentChargeMainBinding, ChargeMainUiState, ChargeMainEvent, ChargeMainViewModel>(
    FragmentChargeMainBinding::inflate,
) {
    override val viewModel: ChargeMainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        binding.apply {
            vm = viewModel
            textSelectAccount.setOnClickListener {
                showAccountBottomSheet()
            }

            textStableUsdt.setOnClickListener {
                navigateChart("USDT", viewModel.uiState.value.myUsdtMoney)
            }

            textStableUsdc.setOnClickListener {
                navigateChart("USDC", viewModel.uiState.value.myUsdcMoney)
            }
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiEvent.collect {

                }
            }

            launch {
                viewModel.uiState.collect {
                    binding.textStableUsdtMoney.text = "${it.usdtInfo.price}원"
                    binding.textStableUsdtMoneyPercent.text = it.usdtInfo.rate
                    binding.textStableUsdtMoneyPercent.setTextColor(it.usdtInfo.color)

                    binding.textStableUsdcMoney.text = "${it.usdcInfo.price}원"
                    binding.textStableUsdcMoneyPercent.text = it.usdcInfo.rate
                    binding.textStableUsdcMoneyPercent.setTextColor(it.usdcInfo.color)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showAccountBottomSheet() {
        SelectAccountBottomSheet(
            onClickItem = {
                if (it == AccountType.UPBIT) {
                    viewModel.getUpbit()
                    binding.imageAccout.setImageResource(R.drawable.ic_upbit_logo)
                    binding.textAccount.text = "업비트"
                } else {
                    viewModel.getBithumb()
                    binding.imageAccout.setImageResource(R.drawable.ic_bitsum_logo)
                    binding.textAccount.text = "빗썸"
                }
            }
        ).show(parentFragmentManager, "")
    }

    private fun navigateChart(coin: String, coinCount: String) {
        val action = ChargeMainFragmentDirections.actionChargeMainFragmentToChargeFragment(coin, money = viewModel.uiState.value.myKrwMoney, coinCount = coinCount, tradeType = viewModel.uiState.value.selectTradeType)
        findNavController().navigate(action)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        viewModel.startCoinMonitoring()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        viewModel.stopCoinMonitoring()
    }
}