package com.stable.scoi.presentation.ui.transfer

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.viewModelScope
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.domain.model.transfer.ExecuteRequest
import com.stable.scoi.domain.model.transfer.QuoteRequest
import com.stable.scoi.domain.model.transfer.ValidateRequest
import com.stable.scoi.domain.repository.transfer.BalancesRepository
import com.stable.scoi.domain.repository.transfer.DirectoryRepository
import com.stable.scoi.domain.repository.transfer.ExecuteRepository
import com.stable.scoi.domain.repository.transfer.QuoteRepository
import com.stable.scoi.domain.repository.transfer.ValidateRepository
import com.stable.scoi.presentation.ui.transfer.bottomsheet.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val directoryRepository: DirectoryRepository,
    private val validateRepository: ValidateRepository,
    private val executeRepository: ExecuteRepository,
    private val quoteRepository: QuoteRepository,
    private val balancesRepository: BalancesRepository) : BaseViewModel<TransferState, TransferEvent>(
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

    private val _myExchange = MutableStateFlow("")
    val myExchange = _myExchange.asStateFlow()

    private val _myAssetSymbol = MutableStateFlow<String>("")
    val myAssetSymbol = _myAssetSymbol.asStateFlow()

    private val _myAddress = MutableStateFlow("")
    val myAddress = _myAddress.asStateFlow()



    //API 요구 정보
    private val _receiver = MutableStateFlow(ValidateRequest())
    val receiver = _receiver.asStateFlow()

    private val _information = MutableStateFlow(Information())
    val information = _information.asStateFlow()

    private val _execute = MutableStateFlow(ExecuteRequest())
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
        if(_receiver.value.recipientKoName == "" ||
            _receiver.value.recipientEnName == "" ||
            _receiver.value.walletAddress == "" ||
            _exchangeType.value == Exchange.Empty ||
            _exchangeType.value == Exchange.Unselected
        ) {
            Unit
        }
        else {
            judgeValidate(receiver.value)
        }
    }


    //Receiver
    fun submitReceiver(receiverKORName: String, receiverENGName: String, receiverAddress: String) {
        _receiver.value = _receiver.value.copy(
            recipientKoName = receiverKORName,
            recipientEnName = receiverENGName,
            walletAddress = receiverAddress,
            exchangeType = exType,
            coinType = asSymb
        )
    }

    //Information
    fun submitInformation(amount: String) {
        _information.value = _information.value.copy(
            amount
        )
    }

    //Password
    fun submitPassword(
        first: String, second: String, third:String, fourth: String, fifth: String, sixth: String) {
        val simplePassword = first + second + third + fourth + fifth + sixth
        val network = when (netWorkType.value) {
            Network.TRON -> "TRX"
            Network.KAIA -> "KAIA"
            Network.APTOS -> "APT"
            Network.ETHEREUM -> "ETH"
        }
        val idempotencyKey = UUID.randomUUID().toString()

        _execute.value = _execute.value.copy(
            asSymb,
            network,
            information.value.amount,
            receiver.value.walletAddress,
            receiver.value.exchangeType,
            "INDIVIDUAL",
            receiver.value.recipientKoName,
            receiver.value.recipientEnName,
            simplePassword,
            idempotencyKey
        )
        execute(execute.value)
    }

    //Network
    fun submitNetwork(network: Network) {
        _networkType.value = network
    }


    //API
    fun setDirectoryList(exchange: String, coinType: String) = viewModelScope.launch {
        resultResponse(
            response = directoryRepository.loadDirectoryList(exchange, coinType),

            successCallback = { directoryListResponse ->
                updateState {
                    copy(
                        directoryList = directoryListResponse.result,
                    )
                }
            }
        )
    }

    fun judgeValidate(request: ValidateRequest) = viewModelScope.launch {
        resultResponse(
            response = validateRepository.judgeValidateRecipient(request),

            successCallback = { validateResponse ->
                updateState {
                    copy(
                        validateBalance = validateResponse.balance
                    )
                }
                emitEvent(TransferEvent.NavigateToNextPage)
            },

            errorCallback = { failState ->
                emitEvent(TransferEvent.ShowError(failState))
            }
        )
    }

    fun execute(request: ExecuteRequest) = viewModelScope.launch {
        resultResponse(
            response = executeRepository.execute(request),

            successCallback = { executeResponse ->
                emitEvent(TransferEvent.NavigateToNextPage)
            },

            errorCallback = { failState ->
                emitEvent(TransferEvent.ShowError(failState))
            }
        )
    }

    fun quote(request: QuoteRequest) = viewModelScope.launch {
        resultResponse(
            response = quoteRepository.quote(request),

            successCallback = { quoteResponse ->
                emitEvent(TransferEvent.NavigateToNextPage)
            },

            errorCallback = { failState ->
                emitEvent(TransferEvent.ShowError(failState))
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

    fun formattedNetwork(network: Network): String {
        return when (network) {
            Network.TRON -> "TRX"
            Network.KAIA -> "KAIA"
            Network.APTOS -> "APT"
            Network.ETHEREUM -> "ETH"
        }
    }


}