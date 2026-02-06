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

    fun onPinChanged(input: String) {
        updateState {
            this.copy(
                pin = input,
                isButtonEnabled = input.length == 6
            )
        }
    }

    // 2. '입력하기' 버튼 클릭 시 (비밀번호 검증을 거치도록 수정)
    fun onCompleteClicked() {
        if (!uiState.value.isLoading) {
            login(uiState.value.pin)
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

}