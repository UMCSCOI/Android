package com.stable.scoi.presentation.mypage

import androidx.lifecycle.viewModelScope
import com.stable.scoi.data.api.*
import com.stable.scoi.domain.repository.MyPageRepository
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import com.stable.scoi.presentation.base.UiEvent

data class MyPageUiState(
    val userInfo: UserResponse? = null,
    val apiKeys: List<ApiKeyInfo> = emptyList(),
    val exchangeList: List<ExchangeInfo> = emptyList(),
    val isLoading: Boolean = false
) : UiState

sealed class MyPageEvent : UiEvent {
    object SaveSuccess : MyPageEvent()
    object DeleteSuccess : MyPageEvent()
    object PasswordChangeSuccess : MyPageEvent()
    data class ShowToast(val message: String) : MyPageEvent()
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val repository: MyPageRepository
) : BaseViewModel<MyPageUiState, MyPageEvent>(MyPageUiState()) {

    fun loadUserInfo() {
        viewModelScope.launch {
            resultResponse(repository.getMyInfo(), { data ->
                updateState { copy(userInfo = data) }
            })
        }
    }

    fun loadApiSettings() {
        viewModelScope.launch {
            resultResponse(repository.getExchangeList(), { list ->
                updateState { copy(exchangeList = list) }
            })
            resultResponse(repository.getApiKeys(), { result ->
                updateState { copy(apiKeys = result.apiKeys) }
            })
        }
    }

    fun saveApiKeys(
        upbitPublic: String, upbitSecret: String,
        bithumbPublic: String, bithumbSecret: String
    ) {
        viewModelScope.launch {
            val requests = mutableListOf<ApiKeyRequest>()

            if (upbitPublic.isNotEmpty() || upbitSecret.isNotEmpty()) {
                requests.add(ApiKeyRequest("UPBIT", upbitPublic, upbitSecret)) // 암호화 필요
            }
            if (bithumbPublic.isNotEmpty() || bithumbSecret.isNotEmpty()) {
                requests.add(ApiKeyRequest("BITHUMB", bithumbPublic, bithumbSecret)) // 암호화 필요
            }

            if (requests.isNotEmpty()) {
                updateState { copy(isLoading = true) }
                resultResponse(
                    repository.registerApiKeys(requests),
                    successCallback = {
                        updateState { copy(isLoading = false) }
                        emitEvent(MyPageEvent.SaveSuccess)
                    },
                    errorCallback = {
                        updateState { copy(isLoading = false) }
                        emitEvent(MyPageEvent.ShowToast("저장에 실패했습니다."))
                    }
                )
            }
        }
    }

    fun deleteApiKey(exchangeType: String) {
        viewModelScope.launch {
            resultResponse(repository.deleteApiKey(exchangeType), {
                emitEvent(MyPageEvent.DeleteSuccess)
                loadApiSettings()
            })
        }
    }

    fun changePassword(oldPw: String, newPw: String) {
        val request = PasswordChangeRequest(oldPw, newPw)
        viewModelScope.launch {
            resultResponse(repository.changePassword(request), {
                emitEvent(MyPageEvent.PasswordChangeSuccess)
            }, {
                emitEvent(MyPageEvent.ShowToast("비밀번호 변경 실패"))
            })
        }
    }
}