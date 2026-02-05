package com.stable.scoi.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.stable.scoi.domain.model.CandleStreamEvent
import com.stable.scoi.domain.repository.DummyRepository
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiEvent
import com.stable.scoi.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
) : BaseViewModel<MainActivityUiState, MainActivityEvent>(
    MainActivityUiState()
) {
    fun onClickHome() {
        updateState {
            copy(
                isHome = true,
                isCharge = false,
                isWallet = false
            )
        }
    }

    fun onClickCharge() {
        updateState {
            copy(
                isHome = false,
                isCharge = true,
                isWallet = false
            )
        }
    }

    fun onClickWallet() {
        updateState {
            copy(
                isHome = false,
                isCharge = false,
                isWallet = true
            )
        }
    }
}

data class MainActivityUiState(
    val isHome: Boolean = true,
    val isCharge: Boolean = false,
    val isWallet: Boolean = false
) : UiState

sealed class MainActivityEvent : UiEvent {
    object DummyEvent: MainActivityEvent()
}