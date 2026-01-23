package com.stable.scoi.presentation.base

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale

@HiltViewModel
class TransferViewModel @Inject constructor() : BaseViewModel<TransferState, TransferEvent>(
    TransferState()
) {
    private var exType: String = ""
    private var asSymb: String = ""



    private val _assetSymbolEvent = MutableLiveData<TransferEvent>(TransferEvent.Null)
    val assetSymbolEvent: LiveData<TransferEvent> = _assetSymbolEvent

    private val _sendCheckEvent = MutableLiveData<TransferEvent>()
    val sendCheckEvent: LiveData<TransferEvent> = _sendCheckEvent

    private val _receiverType = MutableStateFlow<ReceiverType>(ReceiverType.Empty)
    val receiverType = _receiverType.asStateFlow()

    private val _exchangeType = MutableStateFlow<Exchange>(Exchange.Empty)
    val exchangeType = _exchangeType.asStateFlow()
    private val _receiver = MutableStateFlow(Receiver())
    val receiver = _receiver.asStateFlow()

    private val _information = MutableStateFlow(Information())
    val information = _information.asStateFlow()

    private val _execute = MutableStateFlow(Execute())
    val execute = _execute.asStateFlow()

    private val _assetSymbolType = MutableStateFlow<AssetSymbol>(AssetSymbol.Empty)
    val assetSymbolType = _assetSymbolType.asStateFlow()


    //Event -> emitEvent로 교체 필요
    private val _nextEvent = MutableStateFlow<TransferEvent>(TransferEvent.Null)
    val nextEvent = _nextEvent.asStateFlow()

    private val _receiverTypeEvent = MutableStateFlow<TransferEvent>(TransferEvent.Null)
    val receiverTypeEvent = _receiverTypeEvent.asStateFlow()


    //Event
    fun onReceiverTypeClicked() { //초기 receiverType 결정
        when (receiverType.value) {
            ReceiverType.Empty -> {
                _receiverTypeEvent.value = TransferEvent.Submit
            }
            else -> Unit
        }
    }
    fun onReceiverTypeChange() { //우측 receiverType 결정 메뉴
        _receiverTypeEvent.value = TransferEvent.Submit
    }

    fun eventCancel() {
        _nextEvent.value = TransferEvent.Cancel
    }

    //ReceiverType
    fun setReceiverTypeIndividual() {
        _receiverType.value = ReceiverType.Individual
        Log.d("receiverType", receiverType.value.toString())
    }

    fun setReceiverTypeCorporation() {
        _receiverType.value = ReceiverType.Corporation
        Log.d("receiverType", receiverType.value.toString())

    }


    //ExchangeType
    fun setExchangeUpbit() {
        _exchangeType.value = Exchange.Upbit
        exType = "UPBIT"
    }
    fun setExchangeBithumb() {
        _exchangeType.value = Exchange.Bithumb
        exType = "BITHUMB"
    }
    fun setExchangeBinance() {
        _exchangeType.value = Exchange.Binance
        exType = "BINANCE"
    }
    fun setExchange() {
        _exchangeType.value = Exchange.Unselected
    }


    //AssetSymbolType
    fun setAssetSymbolUSDT() {
        _assetSymbolType.value = AssetSymbol.USDT
        asSymb = "USDT"
    }

    fun setAssetSymbolUSDC() {
        _assetSymbolType.value = AssetSymbol.USDC
        asSymb = "USDC"
    }


    //NextButton
    fun onClickNextButton() {
        if(_receiver.value.receiverName == null ||
            _receiver.value.receiverName == "" ||
            _receiver.value.receiverAddress == null ||
            _receiver.value.receiverAddress == "" ||
            _exchangeType.value == Exchange.Empty ||
            _exchangeType.value == Exchange.Unselected ||
            _receiverType.value == ReceiverType.Empty
        ) {
           Unit
        }
        else _nextEvent.value = TransferEvent.Submit
    }


    //Receiver
    fun submitReceiver(receiverName: String, receiverAddress: String) {
        _receiver.value = _receiver.value.copy(
            receiverName,
            receiverAddress,
            _receiverType.value
        )
    }

    //Information
    fun submitInformation(amount: String) {
        _information.value = _information.value.copy(
            exType,
            asSymb,
            amount
        )
    }

    //Password
    fun submitPassword(
        first: String, second: String, third:String, fourth: String, fifth: String, sixth: String) {
        val simplePassword = first + second + third + fourth + fifth + sixth

        _execute.value = _execute.value.copy(
            simplePassword
        )
    }

    //ETC
    fun addressLineChange(address: String): String {
        return address.chunked(22)
            .joinToString("\n")
    }

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