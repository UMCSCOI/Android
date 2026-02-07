package com.stable.scoi.presentation.ui.wallet

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.ui.transfer.Exchange
import com.stable.scoi.presentation.ui.wallet.recyclerview.chargeList.RecentChargeList
import com.stable.scoi.presentation.ui.wallet.recyclerview.transferList.RecentTransferList
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale

@HiltViewModel
class WalletViewModel @Inject constructor(): BaseViewModel<WalletState, WalletEvent>(WalletState()) {

    private var exType: String = ""
    private val _exchangeType = MutableStateFlow<Exchange>(Exchange.Empty)
    val exchangeType = _exchangeType.asStateFlow()

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
}