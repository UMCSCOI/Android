package com.stable.scoi.presentation.ui.login

import androidx.lifecycle.viewModelScope
import com.stable.scoi.data.api.PasswordReResetRequest
import com.stable.scoi.data.local.PreferenceManager
import com.stable.scoi.data.util.EncryptionUtil
import com.stable.scoi.domain.repository.auth.AuthRepository
import com.stable.scoi.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel<LoginState, LoginEvent>(LoginState()) {

    init {
        checkAutoLoginStatus()
    }

    private fun checkAutoLoginStatus() {

        val token = preferenceManager.getAccessToken()
        if (token.isEmpty()) {

        }
    }


    // 완료 버튼 눌렀을 때
    fun onCompleteClicked() {
        if (!uiState.value.isLoading && uiState.value.isButtonEnabled) {
            tryLogin(uiState.value.simplePassword)
        }
    }

    fun onBiometricLogin() {
        emitEvent(LoginEvent.NavigationToBiometric)
    }

    private fun tryLogin(pin: String) {
        viewModelScope.launch {
            updateState { this.copy(isLoading = true) }

            val savedPhoneNumber = preferenceManager.getPhoneNumber()
            val verificationToken = preferenceManager.getVerificationToken()

            if (savedPhoneNumber.isEmpty()) {
                updateState { this.copy(isLoading = false) }
                emitEvent(LoginEvent.ShowError("저장된 사용자 정보가 없습니다."))
                return@launch
            }

            val encryptedPin = EncryptionUtil.encrypt(pin)

            authRepository.pinLogin(
                phoneNumber = savedPhoneNumber,
                simplePassword = encryptedPin,
                verificationToken = verificationToken
            ).onSuccess {

                emitEvent(LoginEvent.NavigationToMain)
            }.onFailure { e ->

                if (e is retrofit2.HttpException) {
                    val errorJson = e.response()?.errorBody()?.string()
                    val jsonObject = if (!errorJson.isNullOrEmpty()) JSONObject(errorJson) else null

                    val code = jsonObject?.optString("code") ?: ""

                    val resultObj = jsonObject?.optJSONObject("result")

                    val smsRequired = resultObj?.optString("smsRequired") ?: "false"

                    when {
                        code == "AUTH403_1" && smsRequired == "true" -> {
                            emitEvent(LoginEvent.ShowAccountLockedDialog)
                        }
                    }
                    updateState { copy(isLoading=false) }
                    updateState { copy(simplePassword = "", isButtonEnabled = false) }
                }
            }
        }
    }

     fun onReLogin() {
        viewModelScope.launch {

            updateState { this.copy(isLoading = true) }

            val phoneNumber = preferenceManager.getPhoneNumber()
            val verificationToken = preferenceManager.getVerificationToken()
            val newPin = uiState.value.simplePassword

            if (phoneNumber.isEmpty() || verificationToken.isEmpty()) {
                updateState { this.copy(isLoading = false) }
                emitEvent(LoginEvent.ShowError("인증 정보가 만료되었습니다. 다시 시도해주세요."))
                return@launch
            }

            val encryptedPin = EncryptionUtil.encrypt(newPin)

            val request = PasswordReResetRequest(
                phoneNumber = phoneNumber,
                newPassword = encryptedPin,
                verificationToken = verificationToken
            )

            authRepository.resetPW(request)
                .onSuccess { response ->
                    emitEvent(LoginEvent.NavigationToLogin)
                }
                .onFailure { e ->
                    val errorMsg = if (e is retrofit2.HttpException) {
                        val errorJson = e.response()?.errorBody()?.string()
                        JSONObject(errorJson ?: "").optString("message", "재설정 실패")
                    } else {
                        e.message ?: "네트워크 오류가 발생했습니다."
                    }

                    emitEvent(LoginEvent.ShowError(errorMsg))
                    updateState { this.copy(simplePassword = "", isButtonEnabled = false) }
                }

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