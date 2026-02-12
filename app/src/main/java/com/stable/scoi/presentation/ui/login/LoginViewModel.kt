package com.stable.scoi.presentation.ui.login

import androidx.lifecycle.viewModelScope
import com.stable.scoi.data.local.PreferenceManager
import com.stable.scoi.domain.repository.auth.AuthRepository
import com.stable.scoi.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,      // 1. API 통신용
    private val preferenceManager: PreferenceManager // 2. 저장된 전화번호 가져오기용
) : BaseViewModel<LoginState, LoginEvent>(LoginState()) {

    init {
        checkAutoLoginStatus()
    }

    private fun checkAutoLoginStatus() {
        // 토큰이 있는지 확인 (자동 로그인 여부)
        val token = preferenceManager.getAccessToken()
        if (token.isEmpty()) {
            // 토큰 없으면 만료 처리(또는 로그인 화면 유지)
            // emitEvent(LoginEvent.NavigationToExpired) // 기획에 따라 결정
        }
    }


    // 완료 버튼 눌렀을 때
    fun onCompleteClicked() {
        if (!uiState.value.isLoading && uiState.value.isButtonEnabled) {
            tryLogin(uiState.value.simplePassword)
        }
    }

    // 생체 인증 버튼 클릭
    fun onBiometricLogin() {
        emitEvent(LoginEvent.NavigationToBiometric)
    }

    private fun tryLogin(pin: String) {
        viewModelScope.launch {
            // 1. 로딩 시작
            updateState { this.copy(isLoading = true) }

            val savedPhoneNumber = preferenceManager.getPhoneNumber()

            if (savedPhoneNumber.isEmpty()) {
                updateState { this.copy(isLoading = false) }
                emitEvent(LoginEvent.ShowError("저장된 사용자 정보가 없습니다. 다시 가입해주세요."))
                return@launch
            }

            // 3. Repository를 통해 서버 API 호출
            val result = authRepository.pinLogin(
                phoneNumber = savedPhoneNumber,
                simplePassword = pin
            )

            // 4. 결과 처리
            if (result.isSuccess) {
                // 성공 시 메인으로 이동
                emitEvent(LoginEvent.NavigationToMain)
            } else {
                // 실패 시 에러 메시지 띄우기
                val errorMsg = result.exceptionOrNull()?.message ?: "로그인에 실패했습니다."
                emitEvent(LoginEvent.ShowError(errorMsg))

                // (선택) 비밀번호 틀렸으니 입력 초기화
                updateState { this.copy(simplePassword = "", isButtonEnabled = false) }
            }

            // 5. 로딩 종료
            updateState { this.copy(isLoading = false) }
        }
    }

    fun onPhoneAuthNumberChanged(input: String) {
        updateState {
            this.copy(
                phoneNumber = input,
                isCodeSendEnabled = true
            )
        }
    }

    fun onAuthChanged(input: String) {
        this.updateState {
            this.copy(
                verificationCode = input,
                isCodeEnabled = true
            )
        }
    }
    fun onPinChanged(input:String){
        this.updateState {
            this.copy(
                simplePassword = input,
                isButtonEnabled = input.length == 6
            )
        }
    }

    fun onSendClicked() {
        verification(uiState.value.verificationCode)
    }

    private fun verification(code: String) {
        val currentState = uiState.value
        viewModelScope.launch {
           authRepository.verifySms(currentState.phoneNumber,code)
        }
    }
}