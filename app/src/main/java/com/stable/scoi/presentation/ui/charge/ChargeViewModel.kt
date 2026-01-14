package com.stable.scoi.presentation.ui.charge

import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiEvent
import com.stable.scoi.presentation.base.UiState
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
}

data class ChargeUiState(
    val dummy: String = "",
) : UiState

sealed class ChargeEvent : UiEvent {

}