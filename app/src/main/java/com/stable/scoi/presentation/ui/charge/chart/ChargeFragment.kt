package com.stable.scoi.presentation.ui.charge.chart

import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentChargeBinding
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class ChargeFragment :
    BaseFragment<FragmentChargeBinding, ChargeUiState, ChargeEvent, ChargeViewModel>(
        FragmentChargeBinding::inflate,
    ) {
    override val viewModel: ChargeViewModel by activityViewModels()

    private val args: ChargeFragmentArgs by navArgs()

    private val chargeRecentTradeAdapter: ChargeRecentTradeAdapter by lazy {
        ChargeRecentTradeAdapter()
    }

    private val chargePriceAdapter: ChargePriceAdapter by lazy {
        ChargePriceAdapter(
            onClickItem = {
                viewModel.updateMoney(it.price.toInt().toString())
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        binding.apply {
            vm = viewModel
            viewModel.setMyKrwMoney(money = args.money)
            viewModel.setMyCoinCount(count = args.coinCount)
            viewModel.setTradeType(tradeType = args.tradeType)
            setSummaryCountUi()

            if (args.coin == "USDT") {
                textAmount.text = "체결량(USDT)"
                imageStableUsdt.setImageResource(R.drawable.ic_usdt)
                textStableUsdt.text = "USDT"
            } else {
                textAmount.text = "체결량(USDC)"
                imageStableUsdt.setImageResource(R.drawable.ic_usdc)
                textStableUsdt.text = "USDC"
            }

            tvChartWebView.setupTvChart(onReady = {
                val coin = if (args.coin == "USDT") "KRW-USDT" else "KRW-USDC"
                viewModel.startMonitoring(coin)
            })


            layoutChargeMoney.setOnClickListener {
                setEditingUi()
                setSummaryCountUi()

                editMoney.isFocusableInTouchMode = true
                editMoney.requestFocus()
                showKeyboard(editMoney)
            }

            layoutChargeCount.setOnClickListener {
                setSummaryUi()
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

            recyclerPrice.apply {
                adapter = chargePriceAdapter
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
                    when(it){
                        ChargeEvent.MoveToBack -> findNavController().popBackStack()
                        is ChargeEvent.ShowLackMoneyEvent -> showLackBottomSheet(it.lackMoney)
                        ChargeEvent.ShowExceedCountEvent -> showExceedBottomSheet()
                        ChargeEvent.ShowChargeBottomSheet -> showChargeBottomSheet()
                        ChargeEvent.Complete -> {}
                    }
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

                        is CandleStreamEvent.TickerUpdate -> {
                            updateLeftPanel(ev.ticker)
                            updatePriceList(ev.ticker)
                        }
                    }

                }
            }

            launch {
                viewModel.uiState.collect {
                    binding.textStableUsdtMoney.text = "${it.coin.price}원"
                    binding.textStableUsdtMoneyPercent.text = it.coin.rate
                    binding.textStableUsdtMoneyPercent.setTextColor(it.coin.color)

                    if (it.inputType == ChargeInputType.SELECT) {
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
            val imm =
                target.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(target, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyboard(target: View) {
        val imm =
            target.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
            layoutChargeMoney.setBackgroundResource(R.drawable.bg_rect_white_stroke_disable_radius10)
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
            textCountTitle.setTextColor(ContextCompat.getColor(requireActivity(), R.color.main_black))

            textCount.gone()
        }
    }

    fun setSummaryCountUi() {
        binding.apply {
            textMax.gone()
            textCount.visible()
            textTotal.gone()
            textCountTitle.setTextColor(ContextCompat.getColor(requireActivity(), R.color.sub_gray_1))

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

        val layoutManager = binding.recyclerRecentAmount.layoutManager as LinearLayoutManager
        val isAtTop = layoutManager.findFirstVisibleItemPosition() == 0

        chargeRecentTradeAdapter.submitList(newList) {
            if (isAtTop) {
                binding.recyclerRecentAmount.scrollToPosition(0)
            }
        }
    }

    private fun updatePriceList(ticker: UpbitTicker) {
        val currentPrice = ticker.tradePrice
        val prevClose = currentPrice / (1 + ticker.signedChangeRate)

        val priceList = ArrayList<PriceItem>()
        val step = 1.0 // 호가 단위 (1원)

        // 1. 매도 호가 (현재가보다 높은 가격) : 위에서부터 내림차순 (예: +4 ~ +1)
        for (i in 4 downTo 1) {
            val price = currentPrice + (i * step)
            priceList.add(PriceItem(price, isCurrentPrice = false, prevClosingPrice = prevClose))
        }

        priceList.add(PriceItem(currentPrice, isCurrentPrice = true, prevClosingPrice = prevClose))

        for (i in 1..4) {
            val price = currentPrice - (i * step)
            priceList.add(PriceItem(price, isCurrentPrice = false, prevClosingPrice = prevClose))
        }

        chargePriceAdapter.submitList(priceList)
    }

    private fun updateLeftPanel(ticker: UpbitTicker) {
        val nf = NumberFormat.getNumberInstance(Locale.KOREA).apply {
            maximumFractionDigits = 0
        }

        binding.apply {
            // 1. 거래량 (24H)
            val coinType = if (args.coin == "USDT") "USDT" else "USDC"
            textAllAmount.text = "${nf.format(ticker.accTradeVolume24h)} $coinType"
            textHighPrice.text = "고가(당일) ${nf.format(ticker.highPrice)}"
            textLowPrice.text = "저가(당일) ${nf.format(ticker.lowPrice)}"
            textEndPrice.text = "전일종가    ${nf.format(ticker.prevClosingPrice)}"
        }
    }

    private fun showLackBottomSheet(lackMoney: String) {
        LackMoneyBottomSheet(
            money = lackMoney,
            onClickFill = {
                val action = ChargeFragmentDirections.actionChargeToMyWallet()
                findNavController().navigate(action)
            }
        ).show(parentFragmentManager, "LackMoneyBottomSheet")
    }

    private fun showExceedBottomSheet() {
        ExceedBottomSheet().show(parentFragmentManager, "")
    }

    private fun showChargeBottomSheet() {
        val state = viewModel.uiState.value
        ChargeBottomSheet(
            money = state.money,
            count = state.count,
            coin = args.coin,
            total = state.total,
            type = state.pageType,
            onClickRight = {
                findNavController().navigate(R.id.charge_pw_fragment)
            }
        ).show(parentFragmentManager, "")
    }
}