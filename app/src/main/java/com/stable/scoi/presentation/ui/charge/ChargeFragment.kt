package com.stable.scoi.presentation.ui.charge

import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentChargeBinding
import com.stable.scoi.domain.model.CandleStreamEvent
import com.stable.scoi.domain.model.RecentTrade
import com.stable.scoi.domain.model.enums.ChargeInputType
import com.stable.scoi.extension.gone
import com.stable.scoi.extension.setupTvChart
import com.stable.scoi.extension.tvSetData
import com.stable.scoi.extension.tvUpdate
import com.stable.scoi.extension.visible
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.charge.adapter.ChargeRecentTradeAdapter
import com.stable.scoi.util.Format.formatWon
import com.stable.scoi.util.Format.unformatWon
import com.stable.scoi.util.SLOG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChargeFragment : BaseFragment<FragmentChargeBinding, ChargeUiState, ChargeEvent, ChargeViewModel>(
    FragmentChargeBinding::inflate,
) {
    override val viewModel: ChargeViewModel by viewModels()

    private val chargeRecentTradeAdapter : ChargeRecentTradeAdapter by lazy {
        ChargeRecentTradeAdapter()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        binding.apply {
            vm = viewModel
            setSummaryCountUi()

            binding.tvChartWebView.setupTvChart(onReady = {
                viewModel.test("KRW-USDC", 1)
            })

            layoutChargeMoney.setOnClickListener {
                setEditingUi()

                editMoney.isFocusableInTouchMode = true
                editMoney.requestFocus()
                showKeyboard(editMoney)
            }

            layoutChargeCount.setOnClickListener {
                setEditingCountUi()

                editCount.isFocusableInTouchMode = true
                editCount.requestFocus()
                showKeyboard(editCount)
            }

            editMoney.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    layoutChargeMoney.setBackgroundResource(R.drawable.bg_rect_white_stroke_disable_radius10)
                    setSummaryUi()
                    val current = editMoney.text?.toString().orEmpty()
                    val formatted = formatWon(current)

                    if (!formatted.isNullOrBlank()) {
                        viewModel.updateMoney(formatted)
                    }
                } else {
                    layoutChargeMoney.setBackgroundResource(R.drawable.bg_rect_white_stroke_active_radius10)
                    val current = viewModel.uiState.value.money
                    val formatted = unformatWon(current)
                    viewModel.updateMoney(formatted)
                }
            }

            editCount.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    layoutChargeCount.setBackgroundResource(R.drawable.bg_rect_white_stroke_disable_radius10)
                    setSummaryCountUi()
                } else {
                    layoutChargeCount.setBackgroundResource(R.drawable.bg_rect_white_stroke_active_radius10)
                }
            }

            recyclerRecentAmount.apply {
                adapter = chargeRecentTradeAdapter
                layoutManager = LinearLayoutManager(context)
                itemAnimator = null
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiEvent.collect {

                }
            }

            launch {
                viewModel.candleEvents.collect { ev ->
                    when (ev) {
                        is CandleStreamEvent.Snapshot -> binding.tvChartWebView.tvSetData(ev.candles)
                        is CandleStreamEvent.Update -> binding.tvChartWebView.tvUpdate(ev.candle)
                        is CandleStreamEvent.TradeUpdate -> {
                            updateTradeList(ev.trade)
                        }
                    }

                }
            }

            launch {
                viewModel.uiState.collect {
                    if (it.inputType == ChargeInputType.SELECT ) {
                        showBottomLayout()
                    } else {
                        hideBottomLayout()
                    }
                }
            }
        }
    }

    private fun showBottomLayout() {
        binding.apply {
            if (editMoney.hasFocus()) {
                layoutTotalAmount.visible()
                layoutRecentAmount.gone()
            } else {
                hideBottomLayout()
            }
        }
    }

    private fun hideBottomLayout() {
        binding.apply {
            layoutTotalAmount.gone()
            layoutRecentAmount.visible()
        }
    }

    private fun showKeyboard(target: View) {
        target.post {
            val imm = target.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(target, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyboard(target: View) {
        val imm = target.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(target.windowToken, 0)
    }

    fun setEditingUi() {
        binding.apply {
            textInputSelf.visible()
            textInputSelect.visible()
            editMoney.visible()
            imageInputMode.visible()

            textInputMoney.gone()
        }
    }

    fun setSummaryUi() {
        binding.apply {
            if (viewModel.uiState.value.inputType == ChargeInputType.SELF) {
                textInputSelf.visible()
                textInputSelect.gone()
            } else {
                textInputSelf.gone()
                textInputSelect.visible()
            }

            textInputMoney.visible()

            editMoney.gone()
            imageInputMode.gone()

            hideKeyboard(editMoney)
            editMoney.clearFocus()
        }
    }

    fun setEditingCountUi() {
        binding.apply {
            textMax.visible()
            editCount.visible()
            textTotal.visible()

            textCount.gone()
        }
    }

    fun setSummaryCountUi() {
        binding.apply {
            textMax.gone()
            textCount.visible()
            textTotal.gone()

            editCount.gone()
            hideKeyboard(editCount)
            editCount.clearFocus()
        }
    }

    private fun updateTradeList(trade: RecentTrade) {
        val old = chargeRecentTradeAdapter.currentList

        val newList = ArrayList<RecentTrade>(minOf(old.size + 1, 20))
        newList.add(trade)

        for (i in 0 until minOf(old.size, 20 - 1)) {
            newList.add(old[i])
        }

        chargeRecentTradeAdapter.submitList(newList)
    }
}