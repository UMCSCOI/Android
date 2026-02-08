package com.stable.scoi.presentation.ui.Auth

import com.stable.scoi.presentation.base.UiState

data class JoinState(
    val verificationCode:String="",
    val phoneNumber:String="",
    val isCodeSendEnabled:Boolean=false,
    val isSend:Boolean=false,
    val isCodeEnabled:Boolean=false,
    val residentNumber: String = ""

) : UiState