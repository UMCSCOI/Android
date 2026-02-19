package com.stable.scoi.presentation.ui.bio

import androidx.lifecycle.viewModelScope
import com.stable.scoi.data.local.PreferenceManager
import com.stable.scoi.data.util.EncryptionUtil
import com.stable.scoi.domain.repository.auth.AuthRepository
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.ui.login.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import org.json.JSONObject

@HiltViewModel
class BioViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val authRepository: AuthRepository
):
    BaseViewModel<BioState, BioEvent>(BioState()) {

        fun tryLogin() {
            viewModelScope.launch {
                updateState { this.copy(isLoading = true) }

                val savedPhoneNumber = preferenceManager.getPhoneNumber()
                val verificationToken = preferenceManager.getVerificationToken()

                if (savedPhoneNumber.isEmpty()) {
                    updateState { this.copy(isLoading = false) }
                    return@launch
                }
                val pin=preferenceManager.getSimplePassword()

                val encryptedPin = EncryptionUtil.encrypt(pin)

                authRepository.pinLogin(
                    phoneNumber = savedPhoneNumber,
                    simplePassword = encryptedPin,
                    verificationToken = verificationToken
                ).onSuccess {
                    emitEvent(BioEvent.NavigationToMain)
                }.onFailure { e ->

                    if (e is retrofit2.HttpException) {
                        val errorJson = e.response()?.errorBody()?.string()
                        val jsonObject = if (!errorJson.isNullOrEmpty()) JSONObject(errorJson) else null

                        val code = jsonObject?.optString("code") ?: ""

                        val resultObj = jsonObject?.optJSONObject("result")

                        val smsRequired = resultObj?.optString("smsRequired") ?: "false"

                        updateState { copy(isLoading=false) }
                         }
                }
            }

    }
    fun onCountOver(count:Int){
        if(count>=5){
            emitEvent(BioEvent.NavigationToPin) // dialog 뜨게 하기
        }
        else{

        }
    }



}