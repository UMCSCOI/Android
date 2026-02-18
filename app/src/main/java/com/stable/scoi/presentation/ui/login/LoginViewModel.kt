package com.stable.scoi.presentation.ui.login

import androidx.lifecycle.viewModelScope
import com.stable.scoi.data.local.PreferenceManager
import com.stable.scoi.data.util.EncryptionUtil
import com.stable.scoi.domain.repository.auth.AuthRepository
import com.stable.scoi.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject // ★ JSON 파싱용
import retrofit2.HttpException // ★ 에러 처리용
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferenceManager // ★ 대문자로 수정됨
) : BaseViewModel<LoginState, LoginEvent>(LoginState()) {

    init {
        checkAutoLoginStatus()
    }

    private fun checkAutoLoginStatus() {
        val token = preferenceManager.getAccessToken()
        if (token.isEmpty()) {
            // 자동 로그인 로직 필요 시 추가
        }
    }

    fun onCompleteClicked() {
            tryLogin(uiState.value.simplePassword)
    }

    fun onBiometricLogin() {
        emitEvent(LoginEvent.NavigationToBiometric)
    }

    private fun tryLogin(pin: String) {
        viewModelScope.launch {
            // 로딩 시작
            updateState { copy(isLoading = true) }

            val savedPhoneNumber = preferenceManager.getPhoneNumber()
            val verificationToken = preferenceManager.getVerificationToken()

            val encryptedPin = EncryptionUtil.encrypt(pin)

            authRepository.pinLogin(
                phoneNumber = savedPhoneNumber,
                simplePassword = encryptedPin,
                verificationToken = verificationToken
            ).onSuccess { response ->

                emitEvent(LoginEvent.NavigationToMain)
            }.onFailure { e ->

                val errorMessage = parseErrorResponse(e)
                emitEvent(LoginEvent.ShowError(errorMessage))
                updateState { copy(simplePassword = "", isButtonEnabled = false) }
            }

            updateState { copy(isLoading = false) }
        }
    }

    // --- 에러 응답 파싱 (남은 횟수 추출) ---
    private fun parseErrorResponse(e: Throwable): String {
        return try {
            if (e is HttpException) {
                val errorJson = e.response()?.errorBody()?.string()

                if (!errorJson.isNullOrEmpty()) {
                    val jsonObject = JSONObject(errorJson)
                    val code = jsonObject.optString("code")
                    val message = jsonObject.optString("message")

                    // 비밀번호 불일치 에러 코드 확인
                    if (code == "AUTH401_1") {
                        val resultObj = jsonObject.optJSONObject("result")
                        val remain = resultObj?.optInt("remainingAttempts") ?: 0
                        return "$message (남은 기회: ${remain}회)"
                    } else {
                        return message
                    }
                }
            }
            e.message ?: "로그인에 실패했습니다."
        } catch (ex: Exception) {
            "로그인 중 오류가 발생했습니다."
        }
    }

    // --- 입력 핸들러 ---
    fun onPinChanged(input: String) {
        this.updateState {
            this.copy(
                simplePassword = input,
                isButtonEnabled = input.length == 6
            )
        }
    }

    fun onPhoneAuthNumberChanged(input: String) {
        updateState { this.copy(phoneNumber = input, isCodeSendEnabled = true) }
    }

    fun onAuthChanged(input: String) {
        this.updateState { this.copy(verificationCode = input, isCodeEnabled = true) }
    }

    fun onSendClicked() {
        verification(uiState.value.verificationCode)
    }

    private fun verification(code: String) {
        val currentState = uiState.value
        viewModelScope.launch {
            authRepository.verifySms(currentState.phoneNumber, code)
        }
    }
}