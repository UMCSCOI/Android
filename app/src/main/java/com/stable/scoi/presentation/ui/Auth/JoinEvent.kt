package com.stable.scoi.presentation.ui.Auth

import com.stable.scoi.presentation.base.UiEvent

sealed interface JoinEvent : UiEvent {
    object VerifySuccess : JoinEvent
    data class ShowError(val message: String): JoinEvent

}