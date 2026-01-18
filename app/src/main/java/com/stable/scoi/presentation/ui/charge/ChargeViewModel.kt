package com.stable.scoi.presentation.ui.charge

import com.stable.scoi.domain.model.enums.ChargeInputType
import com.stable.scoi.domain.model.enums.ChargePageType
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiEvent
import com.stable.scoi.presentation.base.UiState
import com.stable.scoi.util.Format.formatWon
import com.stable.scoi.util.Format.unformatWon
import com.stable.scoi.util.SLOG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChargeViewModel
@Inject
constructor() : BaseViewModel<ChargeUiState, ChargeEvent>(
    ChargeUiState(),
) {
    init {
    }
    fun test() {

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

        SLOG.D(total.toString())
        SLOG.D(unformatWon(uiState.value.money))
        SLOG.D(uiState.value.count)

        updateState {
            copy(
                count = count,
                total = "총 ${formatWon(total.toString())}"
            )
        }
    }

    fun updateInputMode() {
        val mode = if(uiState.value.inputType == ChargeInputType.SELF) ChargeInputType.SELECT else ChargeInputType.SELF
        updateState { copy(inputType = mode) }
    }

    fun updateMoney(money: String) {
        updateState { copy(money = money) }
    }

    fun updatePageType(type: ChargePageType){
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