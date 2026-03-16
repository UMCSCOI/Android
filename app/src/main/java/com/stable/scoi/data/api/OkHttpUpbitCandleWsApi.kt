package com.stable.scoi.data.api

import com.stable.scoi.domain.model.UpbitWsEvent
import com.stable.scoi.util.SLOG
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.util.UUID
import javax.inject.Inject

class OkHttpUpbitCandleWsApi @Inject constructor(
    private val client: OkHttpClient
) {

    private var ws: WebSocket? = null

    fun streamMinuteCandle(
        markets: List<String>,
        unitMinutes: Int = 1,
        subscribeCandle: Boolean = true,
        subscribeTrade: Boolean = true,
        subscribeTicker: Boolean = true // [NEW] 현재가(Ticker) 구독 여부
    ): Flow<UpbitWsEvent> = callbackFlow {

        // 기존 연결 정리
        ws?.close(1000, "reconnect")
        ws = null

        val request = Request.Builder()
            .url("wss://api.upbit.com/websocket/v1")
            .build()

        val listener = object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
               // SLOG.D("Quotation WS OPEN")
                trySend(UpbitWsEvent.Open(response.code))

                // [NEW] JSON 생성 함수에도 ticker 파라미터 전달
                val subscribeJson = buildSubscribeJson(
                    markets = markets,
                    unitMinutes = unitMinutes,
                    subscribeCandle = subscribeCandle,
                    subscribeTrade = subscribeTrade,
                    subscribeTicker = subscribeTicker
                )
                webSocket.send(subscribeJson)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                val text = bytes.utf8()
               // SLOG.D("Quotation WS $text") // 로그 너무 많으면 주석 처리
                trySend(UpbitWsEvent.Message(text))
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
               // SLOG.D("Quotation WS $text")
                trySend(UpbitWsEvent.Message(text))
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                SLOG.D("Quotation WS CLOSING: $code / $reason")
                trySend(UpbitWsEvent.Closing(code, reason))
                webSocket.close(code, reason)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                SLOG.D("Quotation WS FAIL: ${t.message}")
                trySend(UpbitWsEvent.Failure(t.message ?: "unknown", response?.code))
            }
        }

        ws = client.newWebSocket(request, listener)

        awaitClose {
            ws?.close(1000, "closed")
            ws = null
        }
    }

    fun close() {
        ws?.close(1000, "manual close")
        ws = null
    }

    private fun buildSubscribeJson(
        markets: List<String>,
        unitMinutes: Int,
        subscribeCandle: Boolean,
        subscribeTrade: Boolean,
        subscribeTicker: Boolean // [NEW]
    ): String {
        val ticket = UUID.randomUUID().toString()
        val codesJson = markets.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }

        val parts = mutableListOf<String>()
        parts += """{"ticket":"$ticket"}"""

        // 1. 캔들 요청
        if (subscribeCandle) {
            val candleType = "candle.${unitMinutes}m"
            parts += """{"type":"$candleType","codes":$codesJson}"""
        }

        // 2. 체결 요청
        if (subscribeTrade) {
            parts += """{"type":"trade","codes":$codesJson}"""
        }

        // 3. [NEW] 현재가(Ticker) 요청
        if (subscribeTicker) {
            parts += """{"type":"ticker","codes":$codesJson}"""
        }

        // 배열로 묶어서 반환: [{"ticket":...}, {"type":"candle..."}, {"type":"ticker..."}]
        return parts.joinToString(prefix = "[", postfix = "]", separator = ",")
    }
}