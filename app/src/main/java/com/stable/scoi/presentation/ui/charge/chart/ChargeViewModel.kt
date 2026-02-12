package com.stable.scoi.presentation.ui.charge.chart

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewModelScope
import com.stable.scoi.data.dto.request.OrderRequest
import com.stable.scoi.data.util.EncryptionUtil
import com.stable.scoi.domain.model.CandleStreamEvent
import com.stable.scoi.domain.model.UpbitTicker
import com.stable.scoi.domain.model.enums.ChargeInputType
import com.stable.scoi.domain.model.enums.ChargePageType
import com.stable.scoi.domain.repository.ChargeRepository
import com.stable.scoi.domain.repository.ChartRepository
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiEvent
import com.stable.scoi.presentation.base.UiState
import com.stable.scoi.presentation.ui.charge.adapter.PriceItem
import com.stable.scoi.presentation.ui.charge.main.CoinInfo
import com.stable.scoi.util.Format.formatWon
import com.stable.scoi.util.Format.unformatWon
import com.stable.scoi.util.SLOG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ChargeViewModel @Inject constructor(
    private val chartRepository: ChartRepository,
    private val chargeRepository: ChargeRepository,
) : BaseViewModel<ChargeUiState, ChargeEvent>(
    ChargeUiState(),
) {

    private var flag: Boolean = false
    private var monitorJob: Job? = null

    private val _candleEvents = MutableSharedFlow<CandleStreamEvent>(
        replay = 0,
        extraBufferCapacity = 64
    )
    val candleEvents: SharedFlow<CandleStreamEvent> = _candleEvents

    fun startMonitoring(chartMarket: String) {
        stopMonitoring()
        updateState { copy(currentMarket = chartMarket) }
        monitorJob = viewModelScope.launch {
            val tickerMarkets = listOf(chartMarket)

            while (isActive) {
                try {
                    chartRepository.streamUnified(chartMarket, tickerMarkets)
                        .collect { ev ->
                            when (ev) {
                                is CandleStreamEvent.Snapshot,
                                is CandleStreamEvent.Update,
                                is CandleStreamEvent.TradeUpdate -> {
                                    _candleEvents.emit(ev)
                                }

                                is CandleStreamEvent.TickerUpdate -> {
                                    _candleEvents.emit(ev)
                                    updateTickerState(ev.ticker)
                                }
                            }
                        }
                } catch (e: Exception) {
                    SLOG.D("ViewModel: Stream Error - ${e.message}")
                }

                delay(3000L)
            }
        }
    }

    fun stopMonitoring() {
        monitorJob?.cancel()
        monitorJob = null
    }

    private fun updateTickerState(ticker: UpbitTicker) {
        val formattedPrice = formatPrice(ticker.tradePrice)
        val formattedRate = formatRate(ticker.signedChangeRate)
        val color = getColorByRate(ticker.signedChangeRate)

        val info = CoinInfo(
            price = formattedPrice,
            rate = formattedRate,
            color = color,
            rawPrice = ticker.tradePrice
        )

        if (flag) {
            updateState {
                copy(coin = info)
            }
        } else {
            val maxCountText = calculateMaxCountText(formattedPrice, uiState.value.myKrwMoney, uiState.value.pageType)
            updateState {
                copy(coin = info, money = formattedPrice, maxCount = maxCountText)
            }
            flag = true
        }
    }

    private fun calculateMaxCountText(priceStr: String, capitalStr: String, pageType: ChargePageType): String {
        val price = priceStr.replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0
        val capital = capitalStr.trim().toDoubleOrNull() ?: 0.0
        val maxCount = if (price > 0) (capital / price).toInt() else 0
        val actionText = if (pageType == ChargePageType.CHARGE) "충전" else "교환"
        return "최대 ${maxCount}개 $actionText 가능"
    }

    fun formatPrice(price: Double): String {
        return if (price >= 100) String.format("%,.0f", price)
        else String.format("%,.2f", price)
    }

    private fun formatRate(rate: Double): String {
        val percent = rate * 100
        return if (percent > 0) String.format("+%.2f%%", percent)
        else String.format("%.2f%%", percent)
    }

    private fun getColorByRate(rate: Double): Int {
        return when {
            rate > 0 -> "#EF2B2A".toColorInt()
            rate < 0 -> "#4A4AFA".toColorInt()
            else -> "#767676".toColorInt()
        }
    }

    fun updateCountChanged() {
        val total = if (uiState.value.money.isNotEmpty() && uiState.value.count.isNotEmpty()) {
            val moneyVal = unformatWon(uiState.value.money).toIntOrNull() ?: 0
            val countVal = uiState.value.count.toIntOrNull() ?: 0
            moneyVal * countVal
        } else {
            0
        }

        updateState {
            copy(
                count = count,
                total = "총 ${formatWon(total.toString())}"
            )
        }
    }

    fun updateInputMode() {
        val mode = if (uiState.value.inputType == ChargeInputType.SELF)
            ChargeInputType.SELECT
        else
            ChargeInputType.SELF
        updateState { copy(inputType = mode) }
    }

    fun updateMoney(money: String) {
        val newMaxCount = calculateMaxCountText(money, uiState.value.myKrwMoney, uiState.value.pageType)
        updateState { copy(money = money, maxCount = newMaxCount) }
        updateCountChanged()
    }

    fun updatePageType(type: ChargePageType) {
        val newMaxCount = calculateMaxCountText(uiState.value.money, uiState.value.myKrwMoney, type)
        updateState { copy(pageType = type, maxCount = newMaxCount) }
    }

    fun back() {
        emitEvent(ChargeEvent.MoveToBack)
    }

    fun setMyKrwMoney(money: String) {
        SLOG.D("하이", money)
        val newMaxCount = calculateMaxCountText(uiState.value.money, money, uiState.value.pageType)
        updateState { copy(myKrwMoney = money, maxCount = newMaxCount) }
    }

    fun onClickButton() {
        val currentState = uiState.value

        if (currentState.pageType == ChargePageType.CHARGE) {
            val price = currentState.money.replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0
            val count = currentState.count.toIntOrNull() ?: 0
            val myAsset = currentState.myKrwMoney.trim().toDoubleOrNull() ?: 0.0
            val totalCost = price * count
            if (totalCost > myAsset) {
                val lackAmount = (totalCost - myAsset).toLong()
                emitEvent(ChargeEvent.ShowLackMoneyEvent(lackAmount.toString()))
            } else {
                emitEvent(ChargeEvent.ShowChargeBottomSheet)
            }
        } else {
            val inputCount = currentState.count.toIntOrNull() ?: 0
            val myHolding = currentState.myCoinCount.replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0
            if (inputCount > myHolding) {
                emitEvent(ChargeEvent.ShowExceedCountEvent)
            } else {
                emitEvent(ChargeEvent.ShowChargeBottomSheet)
            }
        }
    }

    fun setMyCoinCount(count: String) {
        updateState { copy(myCoinCount = count) }
    }

    fun order(password: String) {
        viewModelScope.launch {
            val currentState = uiState.value
            val side = if (currentState.pageType == ChargePageType.CHARGE) "bid" else "ask"
            val finalPrice = currentState.money.replace("[^0-9.]".toRegex(), "")
            val finalVolume = currentState.count.trim()

            val request = OrderRequest(
                tradeType = uiState.value.tradeType,
                market = uiState.value.currentMarket,
                side = side,
                orderType = "limit",
                price = finalPrice,
                volume = finalVolume,
                password = password
            )

             resultResponse(
                 response = chargeRepository.createOrder(request),
                 successCallback = {
                     emitEvent(ChargeEvent.Complete)
                 },
             )
        }
    }

    fun onPinChanged(input: String) {
        SLOG.D("하이 $input")
        updateState { copy(simplePassword = input) }
        if(input.length == 6) {
            order(EncryptionUtil.encrypt(input))
        }
    }

    fun setTradeType(tradeType: String) {
        updateState { copy(tradeType = tradeType) }
    }
}


data class ChargeUiState(
    val pageType: ChargePageType = ChargePageType.CHARGE,
    val inputType: ChargeInputType = ChargeInputType.SELF,
    val currentMarket: String = "",
    val tradeType: String = "",
    val maxCount: String = "최대 0개 충전 가능",
    val money: String = "1447",
    val myKrwMoney: String = "0",
    val myCoinCount: String = "0",
    var count: String = "",
    val total: String = "",
    val simplePassword:String="",
    val coin: CoinInfo = CoinInfo(),
) : UiState

sealed interface ChargeEvent : UiEvent {
    data object MoveToBack: ChargeEvent
    data class ShowLackMoneyEvent(val lackMoney: String): ChargeEvent
    data object ShowExceedCountEvent: ChargeEvent
    data object ShowChargeBottomSheet: ChargeEvent
    data object Complete: ChargeEvent
}