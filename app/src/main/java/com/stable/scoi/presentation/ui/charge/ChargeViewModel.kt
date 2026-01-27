package com.stable.scoi.presentation.ui.charge

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.stable.scoi.domain.model.CandleStreamEvent
import com.stable.scoi.domain.model.ParsedUpbitWs
import com.stable.scoi.domain.model.RecentTrade
import com.stable.scoi.domain.model.TvCandle
import com.stable.scoi.domain.model.enums.ChargeInputType
import com.stable.scoi.domain.model.enums.ChargePageType
import com.stable.scoi.domain.repository.DummyRepository
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiEvent
import com.stable.scoi.presentation.base.UiState
import com.stable.scoi.util.Format.formatWon
import com.stable.scoi.util.Format.unformatWon
import com.stable.scoi.util.SLOG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ChargeViewModel @Inject constructor(
    private val dummyRepository: DummyRepository
) : BaseViewModel<ChargeUiState, ChargeEvent>(
    ChargeUiState(),
) {

    init {
    }

    private val _candleEvents = MutableSharedFlow<CandleStreamEvent>(
        replay = 0,
        extraBufferCapacity = 64
    )
    val candleEvents: SharedFlow<CandleStreamEvent> = _candleEvents

    @RequiresApi(Build.VERSION_CODES.O)
    fun test(market: String, unitMinutes: Int = 1) = viewModelScope.launch {
        dummyRepository.streamMarket(market, unitMinutes, 200)
            .collect { ev ->
                when (ev) {
                    is CandleStreamEvent.Snapshot -> _candleEvents.tryEmit(ev)
                    is CandleStreamEvent.Update -> _candleEvents.tryEmit(ev)
                    is CandleStreamEvent.TradeUpdate -> _candleEvents.tryEmit(ev)
                }
            }
    }

    fun test2() {

    }

    fun updateMoneyChanged() {
        updateState { copy(money = money) }
    }

    fun updateCountChanged() {
        val total = if (uiState.value.money.isNotEmpty() && uiState.value.count.isNotEmpty()) {
            unformatWon(uiState.value.money).toInt() * uiState.value.count.toInt()
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
        val mode =
            if (uiState.value.inputType == ChargeInputType.SELF) ChargeInputType.SELECT else ChargeInputType.SELF
        updateState { copy(inputType = mode) }
    }

    fun updateMoney(money: String) {
        updateState { copy(money = money) }
    }

    fun updatePageType(type: ChargePageType) {
        updateState { copy(pageType = type) }
    }


}

data class ChargeUiState(
    val pageType: ChargePageType = ChargePageType.CHARGE,
    val inputType: ChargeInputType = ChargeInputType.SELF,
    val money: String = "1447",
    var count: String = "",
    val total: String = "",
) : UiState

sealed class ChargeEvent : UiEvent {

}