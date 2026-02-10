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
        viewModelScope.launch {
            resultResponse(
                response = chargeRepository.getMyBalances("Bithumb"),
                successCallback = {
                    val accountVo = AccountCard(
                        type = AccountType.BITSUM,
                        usdc = it.balances.find { it.currency == "USDC" }!!.balance,
                        usdt = it.balances.find { it.currency == "USDT" }!!.balance,
                        key = "입금 주소가 아직 생성되지 않았어요.",
                        isEmpty = false
                    )
                    updateState { copy(firstAccountVo = accountVo) }
                    updateAccountList(uiState.value.accountList + listOf(
                        AccountCard(
                            type = AccountType.BITSUM,
                            usdc = it.balances.find { it.currency == "USDC" }!!.balance,
                            usdt = it.balances.find { it.currency == "USDT" }!!.balance,
                            key = "입금 주소가 아직 생성되지 않았어요.",
                            isEmpty = false
                        )
                    ))
                    getUpbit()
                }
            )
        }
    }

    private fun getUpbit() {
        viewModelScope.launch {
            resultResponse(
                response = chargeRepository.getMyBalances("Upbit"),
                successCallback = {
                    updateAccountList(uiState.value.accountList + listOf(
                        AccountCard(
                            type = AccountType.UPBIT,
                            usdc = it.balances.find { it.currency == "USDC" }!!.balance,
                            usdt = it.balances.find { it.currency == "USDT" }!!.balance,
                            key = "입금 주소가 아직 생성되지 않았어요.",
                            isEmpty = false
                        )
                    ))
                }
            )
        }
    }

    fun updateAccountList(list: List<AccountCard>) {
        updateState { copy(accountList = list) }
    }

    fun onClickSelect() {
        emitEvent(HomeEvent.MoveToTransferEvent)
    }
}

data class HomeUiState(
    val accountList: List<AccountCard> = emptyList(),
    val firstAccountVo: AccountCard = AccountCard()
) : UiState

sealed interface HomeEvent : UiEvent {
    data object MoveToTransferEvent: HomeEvent
}