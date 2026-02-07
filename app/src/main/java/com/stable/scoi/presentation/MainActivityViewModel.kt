package com.stable.scoi.presentation

import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiEvent
import com.stable.scoi.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
) : BaseViewModel<MainActivityUiState, MainActivityEvent>(
    MainActivityUiState()
) {
    fun onClickHome() {
        updateState {
            copy(
                isSplash = false,
                isHome = true,
                isCharge = false,
                isWallet = false
            )
        }
    }

    fun onClickCharge() {
        updateState {
            copy(
                isSplash = false,
                isHome = false,
                isCharge = true,
                isWallet = false
            )
        }
    }

    fun onClickWallet() {
        updateState {
            copy(
                isSplash = false,
                isHome = false,
                isCharge = false,
                isWallet = true
            )
        }
    }
}

data class MainActivityUiState(
    val isSplash: Boolean = true,
    val isHome: Boolean = false,
    val isCharge: Boolean = false,
    val isWallet: Boolean = false
) : UiState

sealed class MainActivityEvent : UiEvent {
    object DummyEvent: MainActivityEvent()
}