package com.stable.scoi.domain.model

sealed class UpbitWsEvent {
    data class Open(val code: Int) : UpbitWsEvent()
    data class Message(val rawJson: String) : UpbitWsEvent()
    data class Closing(val code: Int, val reason: String) : UpbitWsEvent()
    data class Failure(val message: String, val httpCode: Int?) : UpbitWsEvent()
}