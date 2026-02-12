package com.stable.scoi.presentation.ui.my

import androidx.lifecycle.viewModelScope
import com.stable.scoi.data.api.*
import com.stable.scoi.domain.repository.MyPageRepository
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiState
import com.stable.scoi.presentation.base.UiEvent
import com.stable.scoi.presentation.ui.my.info.MyPageEvent
import com.stable.scoi.presentation.ui.my.info.MyPageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val repository: MyPageRepository
) : BaseViewModel<MyPageUiState, MyPageEvent>(MyPageUiState()) {

    fun loadUserInfo() {
        viewModelScope.launch {
            // 로딩 상태 표시 필요 시 updateState { copy(isLoading = true) } 추가
            resultResponse(repository.getMyInfo(), { data ->
                updateState { copy(userInfo = data) }
            })
        }
    }

    // API 설정 화면에서 호출
    fun loadApiSettings() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            // 병렬 처리 혹은 순차 처리
            resultResponse(repository.getExchangeList(), { list ->
                updateState { copy(exchangeList = list) }
            })
            resultResponse(repository.getApiKeys(), { result ->
                updateState { copy(apiKeys = result, isLoading = false) }
            })
        }
    }

    fun saveApiKeys(
        upbitPublic: String, upbitSecret: String,
        bithumbPublic: String, bithumbSecret: String
    ) {
        viewModelScope.launch {
            val requests = mutableListOf<ApiKeyRequest>()

            if (upbitPublic.isNotEmpty() && upbitSecret.isNotEmpty()) {
                requests.add(ApiKeyRequest("UPBIT", upbitPublic, upbitSecret))
            }
            if (bithumbPublic.isNotEmpty() && bithumbSecret.isNotEmpty()) {
                requests.add(ApiKeyRequest("BITHUMB", bithumbPublic, bithumbSecret))
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
            } else {
                emitEvent(MyPageEvent.ShowToast("입력된 키 정보가 없습니다."))
            }
        }
    }

    fun deleteApiKey(exchangeType: String) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            resultResponse(repository.deleteApiKey(exchangeType), {
                emitEvent(MyPageEvent.DeleteSuccess)
                loadApiSettings() // 목록 갱신
            })
        }
    }

    fun changePassword(oldPw: String, newPw: String) {
        val request = PasswordChangeRequest(oldPw, newPw)
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            resultResponse(repository.changePassword(request), {
                updateState { copy(isLoading = false) }
                emitEvent(MyPageEvent.PasswordChangeSuccess)
            }, {
                updateState { copy(isLoading = false) }
                emitEvent(MyPageEvent.ShowToast("비밀번호 변경 실패"))
            })
        }
    }
}