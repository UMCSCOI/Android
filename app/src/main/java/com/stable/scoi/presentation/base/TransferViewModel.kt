package com.stable.scoi.presentation.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class TransferViewModel @Inject constructor() : BaseViewModel<TransferState, TransferEvent>(
    TransferState()
) {
    private val _receiverTypeEvent = MutableLiveData<TransferEvent>()
    val receiverTypeEvent: LiveData<TransferEvent> = _receiverTypeEvent

    private val _exchangeEvent = MutableLiveData<TransferEvent>()
    val exchangeEvent: LiveData<TransferEvent> = _exchangeEvent

    private val _exchangeType = MutableLiveData<Exchange>(Exchange.Null)
    val exchangeType: LiveData<Exchange> = _exchangeType

    private val _nextEvent = MutableLiveData<TransferEvent>()
    val nextEvent: LiveData<TransferEvent> = _nextEvent


    private val _receiverType = MutableStateFlow<ReceiverType>(ReceiverType.Null)
    val receiverType = _receiverType.asStateFlow()

    private val _receiver = MutableStateFlow<Receiver>(Receiver())
    val receiver = _receiver.asStateFlow()


    //Event
    fun onReceiverTypeClicked() { //초기 receiverType 결정
        when (receiverType.value) {
            ReceiverType.Null -> {
                _receiverTypeEvent.value = TransferEvent.Submit
            }
            else -> Unit
        }
    }




    fun submitReceiver(receiverName: String, receiverAddress: String) {
        _receiver.value = _receiver.value.copy(
            receiverName,
            receiverAddress,
            _receiverType.value
        )
    }




    fun onReceiverTypeChange() { //우측 receiverType 결정 메뉴
        _receiverTypeEvent.value = TransferEvent.Submit
    }

    fun onExchangeClicked() {
        _exchangeEvent.value = TransferEvent.Submit
    }

    fun eventCancel() {
        _receiverTypeEvent.value = TransferEvent.Cancel
        _exchangeEvent.value = TransferEvent.Cancel
        _nextEvent.value = TransferEvent.Cancel
    }

    //ReceiverType
    fun setRecieverTypeIndividual() {
        _receiverType.value = ReceiverType.Individual
        Log.d("receiverType", receiverType.value.toString())
    }

    fun setRecieverTypeCorporation() {
        _receiverType.value = ReceiverType.Corporation
        Log.d("receiverType", receiverType.value.toString())

    }


    //ExchangeType
    fun setExchangeUpbit() {
        _exchangeType.value = Exchange.Upbit
    }
    fun setExchangeBithumb() {
        _exchangeType.value = Exchange.Bithumb
    }
    fun setExchangeBinance() {
        _exchangeType.value = Exchange.Binance
    }
    fun setExchange() {
        _exchangeType.value = Exchange.Unselected
    }

    //NextButton
    fun onClickNextButton() {
        if(_receiver.value.receiverName == null ||
            _receiver.value.receiverName == "" ||
            _receiver.value.receiverAddress == null ||
            _receiver.value.receiverAddress == "" ||
            _exchangeType.value == Exchange.Null ||
            _receiverType.value == ReceiverType.Null
        ) {
           Unit
        }
        else _nextEvent.value = TransferEvent.Submit
    }
}