package com.stable.scoi.presentation.ui.home

import com.stable.scoi.domain.model.enums.AccountType
import com.stable.scoi.domain.model.enums.ChargeInputType
import com.stable.scoi.domain.model.enums.ChargePageType
import com.stable.scoi.domain.model.home.AccountCard
import com.stable.scoi.domain.repository.DummyRepository
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiEvent
import com.stable.scoi.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : BaseViewModel<HomeUiState, HomeEvent>(
    HomeUiState(),
) {
    init {
        updateAccountList(getDummy())
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
                key = "TXYZopYRdj2D9XRtbG411XZZc"
            ),
            AccountCard(
                type = AccountType.UPBIT,
                usdc = "20,000",
                usdt = "20,000",
                key = "asdvjzvjnjaksdnjewrewq"
            ),
            AccountCard(
                type = AccountType.BITSUM,
                usdc = "30,000",
                usdt = "30,000",
                key = "123jnjwne12ihhbrhb13"
            )
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