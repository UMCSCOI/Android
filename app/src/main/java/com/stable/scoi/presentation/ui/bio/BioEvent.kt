package com.stable.scoi.presentation.ui.bio

import com.stable.scoi.presentation.base.UiEvent

sealed interface BioEvent : UiEvent {

    object NavigationToPin: BioEvent
    object NavigationToMain: BioEvent
    object ShowError: BioEvent
}