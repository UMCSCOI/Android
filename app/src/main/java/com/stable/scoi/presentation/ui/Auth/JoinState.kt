package com.stable.scoi.presentation.ui.Auth

import com.stable.scoi.data.api.auth.ApiKeyInfo
import com.stable.scoi.presentation.base.UiState

data class JoinState(
    // 회원가입 시
    val koreanName:String="",
    val englishName:String="",
    val residentNumber: String = "",
    val simplePassword: String = "",
    val memberType: String = "INDIVIDUAL",
    val isBioRegistered: Boolean = false,
    val apiKeys: List<ApiKeyInfo> = emptyList(),

    val verificationCode:String="",
    val verificationToken:String="",
    val expiredAt:String="",

    val phoneNumber:String="",
    val isCodeSendEnabled:Boolean=false,
    val isSend:Boolean=false,
    val isCodeEnabled:Boolean=false,

    val isVerified:Boolean=false,
    val isTimerRunning: Boolean = false,
    val leftTime: Long = 0,
    val isLoading: Boolean = false,
    val isButtonEnabled:Boolean=false

    ) : UiState
