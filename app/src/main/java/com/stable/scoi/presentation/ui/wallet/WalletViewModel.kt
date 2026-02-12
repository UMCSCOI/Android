package com.stable.scoi.presentation.ui.wallet

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.viewModelScope
import com.stable.scoi.data.api.transfer.WithDrawRequest
import com.stable.scoi.domain.model.wallet.CancelOrderRequest
import com.stable.scoi.domain.repository.transfer.BalancesRepository
import com.stable.scoi.domain.repository.wallet.CancelOrderRepository
import com.stable.scoi.domain.repository.wallet.TransactionsDetailRepository
import com.stable.scoi.domain.repository.wallet.TransactionsRemitRepository
import com.stable.scoi.domain.repository.wallet.TransactionsTopupsRepository
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.ui.transfer.Exchange
import com.stable.scoi.presentation.ui.wallet.recyclerview.chargeList.RecentChargeList
import com.stable.scoi.presentation.ui.wallet.recyclerview.transferList.RecentTransferList
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val transactionsRemitRepository: TransactionsRemitRepository,
    private val transactionsTopupsRepository: TransactionsTopupsRepository,
    private val transactionsDetailRepository: TransactionsDetailRepository,
    private val cancelOrderRepository: CancelOrderRepository,
    private val balancesRepository: BalancesRepository
): BaseViewModel<WalletState, WalletEvent>(WalletState()) {

    var exType: String = ""
    private val _exchangeType = MutableStateFlow<Exchange>(Exchange.Empty)
    val exchangeType = _exchangeType.asStateFlow()

    private val _arraySettingTransfer = MutableStateFlow(ArraySettingTransfer())
    val arraySettingTransfer =  _arraySettingTransfer.asStateFlow()

    private val _arraySettingCharge = MutableStateFlow(ArraySettingCharge())
    val arraySettingCharge = _arraySettingCharge.asStateFlow()

    private val _transferDetail = MutableStateFlow<RecentTransferList>(RecentTransferList())
    val transferDetail = _transferDetail.asStateFlow()

    private val _chargeDetail = MutableStateFlow<RecentChargeList>(RecentChargeList())
    val chargeDetail = _chargeDetail.asStateFlow()

    private val _amount = MutableStateFlow(0)
    val amount = _amount.asStateFlow()

    fun setExchangeUpbit() {
        _exchangeType.value = Exchange.Upbit
        exType = "UPBIT"
    }
    fun setExchangeBithumb() {
        _exchangeType.value = Exchange.Bithumb
        exType = "BITHUMB"
    }
    fun setExchange() {
        _exchangeType.value = Exchange.Unselected
    }

    fun submitTransferDetails(detail: RecentTransferList) {
        _transferDetail.value = detail
    }

    fun submitChargeDetails(detail: RecentChargeList) {
        _chargeDetail.value = detail
    }

    fun submitAmount(amount: Int) {
        _amount.value = amount
    }

    // [수정] copy를 사용하여 새로운 객체를 할당해야 StateFlow가 변경을 감지함
    fun submitArraySettingTransfer(
        sortType: String,
        categoryType: String,
        periodType: String
    ) {
        _arraySettingTransfer.value = _arraySettingTransfer.value.copy(
            sortType = sortType,
            categoryType = categoryType,
            periodType = periodType
        )
    }

    // [수정] copy를 사용하여 새로운 객체를 할당해야 StateFlow가 변경을 감지함
    fun submitArraySettingCharge(
        sortType: String,
        categoryType: String,
        statusType: String,
        periodType: String
    ) {
        _arraySettingCharge.value = _arraySettingCharge.value.copy(
            sortType = sortType,
            categoryType = categoryType,
            statusType = statusType,
            periodType = periodType
        )
    }

    //API
    fun transactionRemit(
        exchangeType: String,
        type: String,
        period: String,
        order: String,
        limit: Int
    ) = viewModelScope.launch {
        resultResponse(
            response = transactionsRemitRepository.loadTransactionsRemit(exchangeType,type,period,order,limit),

            successCallback = { transactionsRemitResponse ->
                updateState {
                    copy(
                        transactionsRemitItems = transactionsRemitResponse.transactions,
                        totalCount = transactionsRemitResponse.totalCount
                    )
                }
            }
        )
    }

    fun transactionTopups(
        exchangeType: String,
        type: String,
        state: String,
        period: String,
        order: String,
        limit: Int
    ) = viewModelScope.launch {
        resultResponse(
            response = transactionsTopupsRepository.loadTransactionsTopups(exchangeType,type,state,period,order,limit),

            successCallback = { transactionsTopupsResponse ->
                updateState {
                    copy(
                        transactionsTopupsItems = transactionsTopupsResponse.transactions,
                        totalCount = transactionsTopupsResponse.totalCount
                    )
                }
            }
        )
    }


    fun transactionsDetail(
        exchangeType: String,
        category: String,
        remitType: String?,
        uuid: String,
        currency: String?
    ) = viewModelScope.launch {
        resultResponse(
            response = transactionsDetailRepository.loadTransactionsDetail(exchangeType,category,remitType,uuid,currency),

            successCallback = { transactionsDetailResponse ->
                when (transactionsDetailResponse.category) {
                    "REMIT" -> {
                        updateState {
                            copy(
                                transactionsRemitDetailItem = transactionsDetailResponse.remitDetail
                            )
                        }
                    }
                    "TOPUP" -> {
                        updateState {
                            copy(
                                transactionsTopupsDetailItem = transactionsDetailResponse.topupDetail
                            )
                        }
                    }
                }
            }
        )
    }

    fun cancelOrder(request: CancelOrderRequest) = viewModelScope.launch {
        resultResponse(
            response = cancelOrderRepository.cancelOrder(request),

            successCallback = { cancelOrderResponse ->
                updateState {
                    copy(
                        cancelOrderState = cancelOrderResponse.uuid
                    )
                }
            }
        )
    }

    fun balances(exchangeType: String) = viewModelScope.launch {
        resultResponse(
            response = balancesRepository.balances(exchangeType),

            successCallback = { balancesResponse ->
                updateState {
                    copy(
                        balances = balancesResponse.balances
                    )
                }
            }
        )
    }


    //ETC
    fun addComma(amount: String): String {
        if (amount.isBlank()) return ""

        return try {
            NumberFormat.getNumberInstance(Locale.US)
                .format(amount.toLong())
        } catch (e: NumberFormatException) {
            ""
        }
    }

    fun focusRemove(editText: EditText) {
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                editText.clearFocus()
                editText.hideKeyboard()
                true
            }
            else {
                false
            }
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun withDraw() {
        val request = WithDrawRequest(
            exchangeType = exType,
            amount = amount.value,
            MFA = "KAKAO"
        )

        viewModelScope.launch {
            resultResponse(
                response = transactionsRemitRepository.withDraw(request),

                successCallback = { transactionResponse ->
                    // 출금 성공 처리
                    // 예: 완료 메시지 표시, 잔액 갱신, 화면 이동 등
                    // emitEvent(WalletEvent.WithdrawSuccess)

                    // 잔액 갱신
                    balances(exType)
                },

                errorCallback = { failState ->
                    // 실패 처리
                    // emitEvent(WalletEvent.ShowToast(failState.message))
                }
            )
        }
    }

    fun diposit() {
        val request = WithDrawRequest(
            exchangeType = exType,
            amount = amount.value,
            MFA = "KAKAO"
        )

        viewModelScope.launch {
            resultResponse(
                response = transactionsRemitRepository.diposit(request),

                successCallback = { transactionResponse ->
                    balances(exType)
                },

                errorCallback = { failState ->
                    // 실패 처리
                    // emitEvent(WalletEvent.ShowToast(failState.message))
                }
            )
        }
    }
}

data class ArraySettingTransfer(
    var sortType: String = "",
    var categoryType: String = "",
    var periodType: String = ""
)

data class ArraySettingCharge(
    var sortType: String = "",
    var categoryType: String = "",
    var statusType: String = "",
    var periodType: String = ""
)