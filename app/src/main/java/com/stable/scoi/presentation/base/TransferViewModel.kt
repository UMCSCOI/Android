package com.stable.scoi.presentation.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TransferViewModel : BaseViewModel<TransferState, TransferEvent>(initialPageState) {
    private val _recieverTypeEvent = MutableLiveData<TransferEvent>()
    val recieverTypeEvent: LiveData<TransferEvent> = _recieverTypeEvent

    private val _exchangeEvent = MutableLiveData<TransferEvent>()
    val exchangeEvent: LiveData<TransferEvent> = _exchangeEvent

    private val _exchangeType = MutableLiveData<Exchange>()
    val exchangeType: LiveData<Exchange> = _exchangeType

    private val _nextEvent = MutableLiveData<TransferEvent>()
    val nextEvent: LiveData<TransferEvent> = _nextEvent


    private val _recieverType = MutableStateFlow<RecieverType>(RecieverType.Null)
    val recieverType = _recieverType.asStateFlow()


    //Event
    fun onRecieverTypeClicked() { //초기 recieverType 결정
        when (recieverType.value) {
            RecieverType.Null -> {
                _recieverTypeEvent.value = TransferEvent.Submit
            }
            else -> Unit
        }
    }

    fun onRecieverTypeChange() { //우측 recieverType 결정 메뉴
        _recieverTypeEvent.value = TransferEvent.Submit
    }

    fun onExchangeClicked() {
        _exchangeEvent.value = TransferEvent.Submit
    }

    fun eventCancel() {
        _recieverTypeEvent.value = TransferEvent.Cancel
        _exchangeEvent.value = TransferEvent.Cancel
        _nextEvent.value = TransferEvent.Cancel
    }

    //RecieverType
    fun setRecieverTypeIndividual() {
        _recieverType.value = RecieverType.Individual
        Log.d("recieverType", "indiv")
    }

    fun setRecieverTypeCorporation() {
        _recieverType.value = RecieverType.Corporation
        Log.d("recieverType", "corp")

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

    //NextButton
    fun onClickNextButton() {
        _nextEvent.value = TransferEvent.Submit
    }
}