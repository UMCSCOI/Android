package com.stable.scoi.presentation.ui.login

import com.stable.scoi.presentation.base.UiState

// 1. 데이터 클래스는 본문 { }이 아니라 생성자 ( )를 사용해야 합니다.
// 2. BaseFragment가 요구하는 UiState를 상속(구현)해야 합니다.
data class LoginState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccess: Boolean = false,
    val simplePassword:String="",
    val isButtonEnabled: Boolean = false,

    val keepLoined:Boolean=true,
    val phoneNumber:String="",
    val isCodeSendEnabled:Boolean=false,
    val isSend:Boolean=false,
    val isCodeEnabled:Boolean=false,
    val verificationCode:String=""

) : UiState