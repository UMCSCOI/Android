package com.stable.scoi.presentation.ui.login

import androidx.lifecycle.viewModelScope
import com.stable.scoi.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor() :
    BaseViewModel<LoginState, LoginEvent>(LoginState()) {

        init{
            checkAutoLoginStatus()
        }

    private fun checkAutoLoginStatus() {
        val keepLogined = false

        if (!keepLogined) {
            emitEvent(LoginEvent.NavigationToExpired)
        } else {
            // 자동 로그인 로직 수행
        }
    }
    fun onPinChanged(input: String) {
        updateState {
            this.copy(
                simplePassword = input,
                isButtonEnabled = input.length == 6
            )
        }
    }

    fun onCompleteClicked() {
        if (!uiState.value.isLoading) {
            login(uiState.value.simplePassword)
        }
    }

    fun onBiometricLogin(){
        emitEvent(LoginEvent.NavigationToBiometric)
    }

    // 3. 통합 로그인 검증 로직
    private fun login(pin: String) {
        viewModelScope.launch {
            // 로딩 시작 상태 반영
            updateState { this.copy(isLoading = true) }

            delay(1500) // 서버 통신 시뮬레이션

            val isSuccess = pin == "123456" // 설정한 비밀번호

            if (isSuccess) {
                // 성공 시 버튼을 눌렀을 때, 메인 화면으로 이동 가능
                emitEvent(LoginEvent.NavigationToMain)

            } else {
                // 실패 시 로딩 해제 및 에러 이벤트 발송
                updateState { this.copy(isLoading = false) }
                emitEvent(LoginEvent.ShowError("비밀번호가 일치하지 않습니다."))
            }
        }
    }

    //LoginExpiredFragment.kt

    fun onPhoneAuthNumberChanged(input:String){
        updateState {
            this.copy(
                phoneNumber = input,
                isCodeSendEnabled = true
            )
        }
    }

    fun onAuthChanged(input : String){
        this.updateState {
            this.copy(
                verificationCode = input,
                isCodeEnabled = true
            )
        }

    }

    fun onSendClicked() {
            verification(uiState.value.verificationCode)
    }

    private fun verification(code: String) {
        viewModelScope.launch {

            delay(1500)

            if (code == "123456") {
                // 성공 시: 성공 이벤트 발송
                emitEvent(LoginEvent.VerifySuccess)
            } else {
                emitEvent(LoginEvent.ShowError("인증번호가 일치하지 않습니다."))
            }
        }

    }






}