package com.stable.scoi.presentation.ui.home

import androidx.lifecycle.viewModelScope
import com.stable.scoi.domain.model.enums.AccountType
import com.stable.scoi.domain.model.home.AccountCard
import com.stable.scoi.domain.repository.ChargeRepository
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiEvent
import com.stable.scoi.presentation.base.UiState
import com.stable.scoi.util.SLOG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val chargeRepository: ChargeRepository
) : BaseViewModel<HomeUiState, HomeEvent>(
    HomeUiState(),
) {
    var isWalletOpened: Boolean = false

    init {
        updateAccountList(getDummy())

        viewModelScope.launch {
            resultResponse(
                response = chargeRepository.test(),
                successCallback = {
                    SLOG.D("하이")
                }
            )
        }
    }

    fun updateAccountList(list: List<AccountCard>) {
        updateState { copy(accountList = list) }
    }

    fun getDummy(): List<AccountCard> {
        return listOf(
            AccountCard(
                type = AccountType.BITSUM,
                usdc = "10,000",
                usdt = "10,000",
                key = "입금 주소가 아직 생성되지 않았어요."
            ),
            AccountCard(
                type = AccountType.UPBIT,
                usdc = "20,000",
                usdt = "20,000",
                key = "asdvjzvjnjaksdnjewrewq",
                isEmpty = false
            ),
        )
    }

    fun onClickSelect() {
        emitEvent(HomeEvent.MoveToTransferEvent)
    }
}

data class HomeUiState(
    val accountList: List<AccountCard> = emptyList(),
) : UiState

sealed interface HomeEvent : UiEvent {
    data object MoveToTransferEvent: HomeEvent
}