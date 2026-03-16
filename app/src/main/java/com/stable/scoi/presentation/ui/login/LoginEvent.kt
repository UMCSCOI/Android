package com.stable.scoi.presentation.ui.login

import com.stable.scoi.presentation.base.UiEvent

sealed interface LoginEvent : UiEvent{
    object NavigationToMain : LoginEvent
    object NavigationToLogin : LoginEvent

    object NavigationToExpired: LoginEvent
    object VerifySuccess : LoginEvent
    object NavigationToBiometric : LoginEvent

    object ShowAccountLockedDialog : LoginEvent

    data class ShowError(val message: String) : LoginEvent


}