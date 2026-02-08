package com.stable.scoi.presentation.ui.Auth

import androidx.lifecycle.viewModelScope
import com.stable.scoi.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@HiltViewModel
class JoinViewModel @Inject constructor():
    BaseViewModel<JoinState, JoinEvent>(JoinState()) {

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

    fun onSendClicked() {
        verification(uiState.value.verificationCode)
    }

    private fun verification(code: String) {
        viewModelScope.launch {

            delay(1500)

            if (code == "123456") {
                // 성공 시: 성공 이벤트 발송
                emitEvent(JoinEvent.VerifySuccess)
            } else {
                emitEvent(JoinEvent.ShowError("인증번호가 일치하지 않습니다."))
            }
        }
        fun updateResidentNumber(input: String) {
            updateState {
                this.copy(
                    residentNumber = input
                )
            }
        }

    }
}