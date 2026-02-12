package com.stable.scoi.presentation.ui.Auth

import com.stable.scoi.domain.model.enums.JoinInputType
import com.stable.scoi.presentation.base.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed interface JoinEvent : UiEvent {

    data class VerifySuccess(val verificationToken: String) : JoinEvent
    data class OnInputChanged(
        val type: JoinInputType,
        val value: String
    ):JoinEvent


    data class ShowError(val message: String): JoinEvent

    object NavigateToPinRegister : JoinEvent

    object OnSignUpClick: JoinEvent

    object NavigateToRegDone: JoinEvent

    object NavigateToLogin: JoinEvent

    object NavigateToJoinComplete:JoinEvent
}
