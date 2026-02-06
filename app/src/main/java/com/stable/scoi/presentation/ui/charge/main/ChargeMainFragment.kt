package com.stable.scoi.presentation.ui.charge.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.stable.scoi.databinding.FragmentChargeMainBinding
import com.stable.scoi.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChargeMainFragment : BaseFragment<FragmentChargeMainBinding, ChargeMainUiState, ChargeMainEvent, ChargeMainViewModel>(
    FragmentChargeMainBinding::inflate,
) {
    override val viewModel: ChargeMainViewModel by viewModels()

    override fun initView() {
        binding.apply {
            vm = viewModel

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