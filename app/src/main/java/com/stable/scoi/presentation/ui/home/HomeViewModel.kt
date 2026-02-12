package com.stable.scoi.presentation.ui.home

import androidx.lifecycle.viewModelScope
import com.stable.scoi.data.dto.request.CreateAddressRequest
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
        getBithumbAddress()
    }

    private fun getBithumbAddress() = viewModelScope.launch {
        resultResponse(
            response = chargeRepository.getDepositAddress("BITHUMB"),
            successCallback = {
                getBithumb(it)
            }
        )
    }

    private fun getBithumb(address: String) = viewModelScope.launch {
        resultResponse(
            response = chargeRepository.getMyBalances("Bithumb"),
            successCallback = {
                val accountVo = AccountCard(
                    type = AccountType.BITSUM,
                    usdc = it.balances.find { it.currency == "USDC" }!!.balance,
                    usdt = it.balances.find { it.currency == "USDT" }!!.balance,
                    key = address,
                    isEmpty = address.isEmpty()
                )
                updateState { copy(firstAccountVo = accountVo) }
                updateAccountList(uiState.value.accountList + listOf(
                    AccountCard(
                        type = AccountType.BITSUM,
                        usdc = it.balances.find { it.currency == "USDC" }!!.balance,
                        usdt = it.balances.find { it.currency == "USDT" }!!.balance,
                        key = address,
                        isEmpty = address.isEmpty()
                    )
                ))
                getUpbitAddress()
            }
        )
    }

    private fun getUpbitAddress() = viewModelScope.launch {
        resultResponse(
            response = chargeRepository.getDepositAddress("UPBIT"),
            successCallback = {
                getUpbit(it)
            }
        )
    }

    private fun getUpbit(address: String) {
        viewModelScope.launch {
            resultResponse(
                response = chargeRepository.getMyBalances("Upbit"),
                successCallback = {
                    updateAccountList(uiState.value.accountList + listOf(
                        AccountCard(
                            type = AccountType.UPBIT,
                            usdc = it.balances.find { it.currency == "USDC" }!!.balance,
                            usdt = it.balances.find { it.currency == "USDT" }!!.balance,
                            key = address,
                            isEmpty = address.isEmpty()
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

    fun createAddress(coinType: String, netType: String) = viewModelScope.launch {
        val exchangeType = when(uiState.value.accountList[uiState.value.selectPosition].type) {
            AccountType.BITSUM -> "BITHUMB"
            AccountType.UPBIT -> "UPBIT"
        }
        val request = CreateAddressRequest(exchangeType, coinType = listOf(coinType), netType = listOf(netType))
        resultResponse(
            response = chargeRepository.createDepositAddress(request),
            successCallback = {
                getBithumbAddress()
            }
        )
    }

    fun setSelectPosition(selectPosition: Int) {
        updateState { copy(selectPosition = selectPosition) }
    }
}

data class HomeUiState(
    val accountList: List<AccountCard> = emptyList(),
    val firstAccountVo: AccountCard = AccountCard(),
    val selectPosition: Int = 0,
) : UiState

sealed interface HomeEvent : UiEvent {
    data object MoveToTransferEvent: HomeEvent
}