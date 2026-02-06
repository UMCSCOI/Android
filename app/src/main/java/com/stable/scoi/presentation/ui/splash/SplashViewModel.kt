package com.stable.scoi.presentation.ui.splash

import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiEvent
import com.stable.scoi.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
) : BaseViewModel<SplashUiState, SplashUiEvent>(
    SplashUiState(),
) {

}

data class SplashUiState(
    val dummy: String = "",
) : UiState

sealed interface SplashUiEvent : UiEvent {
    data object MoveToHomeEvent: SplashUiEvent
}