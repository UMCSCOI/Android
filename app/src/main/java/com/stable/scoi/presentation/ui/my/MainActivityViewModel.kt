package com.stable.scoi.presentation.ui.my

import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.stable.scoi.presentation.base.UiEvent

@HiltViewModel
class MainActivityViewModel @Inject constructor() : BaseViewModel<MainActivityUiState, MainActivityEvent>(
    MainActivityUiState()
) {
    fun dummyFun() {
        updateState {
            copy(
                dummy = "~~~"
            )
        }
    }

    fun emitFun() {
        emitEvent(MainActivityEvent.DummyEvent)
    }
}

data class MainActivityUiState(
    val dummy: String = ""
) : UiState

sealed class MainActivityEvent : UiEvent {
    object DummyEvent: MainActivityEvent()
}