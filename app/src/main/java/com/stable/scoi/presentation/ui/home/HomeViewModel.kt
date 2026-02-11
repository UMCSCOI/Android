package com.stable.scoi.presentation.ui.home

import androidx.lifecycle.viewModelScope
import com.stable.scoi.domain.model.enums.AccountType
import com.stable.scoi.domain.model.home.AccountCard
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.stable.scoi.domain.repository.MyPageRepository
import kotlinx.coroutines.launch
import com.stable.scoi.presentation.base.UiEvent


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MyPageRepository
) : BaseViewModel<HomeUiState, HomeEvent> (
    HomeUiState(),
) {
    fun test() {
        viewModelScope.launch {
            resultResponse(
                response = repository.getMyInfo(),
                successCallback = {
                    updateState {copy(accountList=emptyList()) }
                },
                errorCallback = {

                }
            )
        }
    }
}

data class HomeUiState(
    val accountList: List<AccountCard> = emptyList(),
) : UiState

sealed interface HomeEvent : UiEvent {
    data object MoveToTransferEvent: HomeEvent
}