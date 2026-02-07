package com.stable.scoi.presentation.ui.transfer

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.domain.model.transfer.Execute
import com.stable.scoi.domain.model.transfer.Information
import com.stable.scoi.domain.model.transfer.Receiver
import com.stable.scoi.presentation.ui.transfer.bottomsheet.Network
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

    //bottomSheet Type 선택

    private val _exchangeType = MutableStateFlow<Exchange>(Exchange.Empty)
    val exchangeType = _exchangeType.asStateFlow()

    private val _assetSymbolType = MutableStateFlow<AssetSymbol>(AssetSymbol.Empty)
    val assetSymbolType = _assetSymbolType.asStateFlow()

    private val _networkType = MutableStateFlow<Network>(Network.TRON)
    val netWorkType = _networkType.asStateFlow()


    //API 요구 정보
    private val _receiver = MutableStateFlow(Receiver())
    val receiver = _receiver.asStateFlow()

    private val _information = MutableStateFlow(Information())
    val information = _information.asStateFlow()

    private val _execute = MutableStateFlow(Execute())
    val execute = _execute.asStateFlow()



    //ExchangeType
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


    //NextButton
    fun onClickNextButton() {
        if(_receiver.value.receiverKORName == null ||
            _receiver.value.receiverKORName == "" ||
            _receiver.value.receiverENGName == null ||
            _receiver.value.receiverENGName == "" ||
            _receiver.value.receiverAddress == null ||
            _receiver.value.receiverAddress == "" ||
            _exchangeType.value == Exchange.Empty ||
            _exchangeType.value == Exchange.Unselected
        ) {
            Unit
        }
        else emitEvent(TransferEvent.NavigateToNextPage)
    }


    //Receiver
    fun submitReceiver(receiverKORName: String, receiverENGName: String, receiverAddress: String) {
        _receiver.value = _receiver.value.copy(
            receiverKORName,
            receiverENGName,
            receiverAddress
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

    //Network
    fun submitNetwork(network: Network) {
        _networkType.value = network
    }


    //API
//    fun setRecentList() {
//        resultResponse(
//            response = TransferRepository.load,
//            successCallback = {
//
//            }
//        )
//    }

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

    fun exchangeToString(exchange: Exchange): String {
        when (exchange) {
            Exchange.Upbit -> {
                val string = "업비트"
                return string
            }
            Exchange.Bithumb -> {
                val string = "빗썸"
                return string
            }
            else -> {
                val string = "잘못된 거래소 정보"
                return string
            }
        }
    }

    fun networkToString(network: Network): String {
        when (network) {
            Network.TRON -> {
                val string = "트론"
                return string
            }
            Network.ETHEREUM -> {
                val string = "이더리움"
                return string
            }
            Network.KAIA -> {
                val string = "카이아"
                return string
            }
            Network.APTOS -> {
                val string = "앱토스"
                return string
            }
        }
    }


}