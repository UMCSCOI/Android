package com.stable.scoi.presentation.ui.charge.chart

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewModelScope
import com.stable.scoi.domain.model.CandleStreamEvent
import com.stable.scoi.domain.model.UpbitTicker
import com.stable.scoi.domain.model.enums.ChargeInputType
import com.stable.scoi.domain.model.enums.ChargePageType
import com.stable.scoi.domain.repository.ChartRepository
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiEvent
import com.stable.scoi.presentation.base.UiState
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
    private val chartRepository: ChartRepository
) : BaseViewModel<ChargeUiState, ChargeEvent>(
    ChargeUiState(),
) {

    // ✅ 하나의 Job으로 통합 관리 (소켓 충돌 방지)
    private var monitorJob: Job? = null

    // 차트 데이터(캔들, 체결 등)를 UI(Fragment)로 보내는 Flow
    private val _candleEvents = MutableSharedFlow<CandleStreamEvent>(
        replay = 0,
        extraBufferCapacity = 64
    )
    val candleEvents: SharedFlow<CandleStreamEvent> = _candleEvents

    fun startMonitoring(chartMarket: String) {
        stopMonitoring() // 기존 연결 정리

        monitorJob = viewModelScope.launch {
            // Ticker(상단 시세)용 마켓 정의
            val tickerMarkets = listOf(chartMarket)

            // 무한 재연결 루프 (While Active)
            while (isActive) {
                try {
                    SLOG.D("ViewModel: Start Connecting Unified Stream...")

                    // Repository의 통합 스트림 호출
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

                // 스트림 종료 시(에러 or 끊김) 3초 대기 후 재연결
                delay(3000L)
            }
        }
    }

    /**
     * 모니터링 중지
     */
    fun stopMonitoring() {
        monitorJob?.cancel()
        monitorJob = null
    }

    // =========================================================================
    //  UI Update Logic
    // =========================================================================

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

        updateState {
            copy(coin = info)
        }
    }

    // =========================================================================
    //  Formatting Helpers
    // =========================================================================

    private fun formatPrice(price: Double): String {
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

    // =========================================================================
    //  User Interaction (기존 유지)
    // =========================================================================

    fun updateMoneyChanged() {
        updateState { copy(money = money) }
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
        updateState { copy(money = money) }
    }

    fun updatePageType(type: ChargePageType) {
        updateState { copy(pageType = type) }
    }

    fun back() {
        emitEvent(ChargeEvent.MoveToBack)
    }
}

// State & Event classes (기존 유지)
data class ChargeUiState(
    val pageType: ChargePageType = ChargePageType.CHARGE,
    val inputType: ChargeInputType = ChargeInputType.SELF,
    val money: String = "1447",
    var count: String = "",
    val total: String = "",
    val coin: CoinInfo = CoinInfo(),
) : UiState

sealed interface ChargeEvent : UiEvent {
    data object MoveToBack: ChargeEvent
}