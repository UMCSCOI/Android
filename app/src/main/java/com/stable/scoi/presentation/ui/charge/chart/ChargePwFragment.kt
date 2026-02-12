package com.stable.scoi.presentation.ui.charge.chart

import android.content.Context
import android.os.Build
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentChargeBinding
import com.stable.scoi.databinding.FragmentChargePwBinding
import com.stable.scoi.domain.model.CandleStreamEvent
import com.stable.scoi.domain.model.RecentTrade
import com.stable.scoi.domain.model.UpbitTicker
import com.stable.scoi.domain.model.enums.ChargeInputType
import com.stable.scoi.extension.gone
import com.stable.scoi.extension.setupTvChart
import com.stable.scoi.extension.tvSetData
import com.stable.scoi.extension.tvUpdate
import com.stable.scoi.extension.visible
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.charge.adapter.ChargePriceAdapter
import com.stable.scoi.presentation.ui.charge.adapter.ChargeRecentTradeAdapter
import com.stable.scoi.presentation.ui.charge.adapter.PriceItem
import com.stable.scoi.presentation.ui.charge.bottomSheet.ChargeBottomSheet
import com.stable.scoi.presentation.ui.charge.bottomSheet.ExceedBottomSheet
import com.stable.scoi.presentation.ui.charge.bottomSheet.LackMoneyBottomSheet
import com.stable.scoi.util.Format.formatWon
import com.stable.scoi.util.Format.unformatWon
import com.stable.scoi.util.SLOG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class ChargePwFragment :
    BaseFragment<FragmentChargePwBinding, ChargeUiState, ChargeEvent, ChargeViewModel>(
        FragmentChargePwBinding::inflate,
    ) {
    override val viewModel: ChargeViewModel by activityViewModels()

    private fun hideKeyboard() {
        val window = requireActivity().window
        WindowInsetsControllerCompat(window, binding.root).hide(WindowInsetsCompat.Type.ime())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        val pinEditTexts = with(binding) {
            listOf(loginPinReg1Et, loginPinReg2Et, loginPinReg3Et, loginPinReg4Et, loginPinReg5Et, loginPinReg6Et)
        }

        pinEditTexts.forEachIndexed { index, editText ->
            editText.doOnTextChanged { text, _, _, _ ->
                // 텍스트가 입력되었을 때만 처리
                if (text?.length == 1) {
                    if (index < 5) {
                        pinEditTexts[index + 1].requestFocus()
                    } else {
                        hideKeyboard()
                    }
                }

                // 데이터 전달
                val currentPin = pinEditTexts.joinToString("") { it.text.toString() }
                viewModel.onPinChanged(currentPin)

                SLOG.D("입력된 핀: $currentPin (현재 인덱스: $index)")
            }

            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    // 현재 칸이 비어있으면 이전 칸으로 이동해서 지우기
                    if (editText.text.isEmpty() && index > 0) {
                        pinEditTexts[index - 1].apply {
                            requestFocus()
                            text = null
                        }
                        return@setOnKeyListener true
                    }
                }
                false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiEvent.collect {
                    when(it) {
                        ChargeEvent.Complete -> findNavController().navigate(R.id.chargeCompleteFragment)
                        else -> {}
                    }
                }
            }

            launch {
                viewModel.candleEvents.collect { ev ->

                }
            }
        }
    }
}