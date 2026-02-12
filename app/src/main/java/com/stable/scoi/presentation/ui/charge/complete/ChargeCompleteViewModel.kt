package com.stable.scoi.presentation.ui.charge.complete

import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiEvent
import com.stable.scoi.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChargeCompleteViewModel @Inject constructor(
) : BaseViewModel<UiState, ChargeCompleteEvent>(
    UiState.Default,
) {

    fun onClickMoveToChargeMain() {
        emitEvent(ChargeCompleteEvent.MoveToChargeMain)
    }

    fun onClickMoveToWallet() {
        emitEvent(ChargeCompleteEvent.MoveToWallet)
    }

}

sealed interface ChargeCompleteEvent : UiEvent {
    data object MoveToChargeMain: ChargeCompleteEvent
    data object MoveToWallet: ChargeCompleteEvent
}