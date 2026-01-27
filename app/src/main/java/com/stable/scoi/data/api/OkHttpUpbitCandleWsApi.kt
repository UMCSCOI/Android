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

    /**
     * ✅ 한 소켓에서 candle + trade 동시에 받기
     *
     * markets: ["KRW-BTC", "KRW-ETH"] 처럼 여러 개 가능
     * unitMinutes: 1, 3, 5, 15 ...
     */
    fun streamMinuteCandle(
        markets: List<String>,
        unitMinutes: Int = 1,
        subscribeCandle: Boolean = true,
        subscribeTrade: Boolean = true,
    ): Flow<UpbitWsEvent> = callbackFlow {

        // 혹시 기존 연결이 있으면 정리
        ws?.close(1000, "reconnect")
        ws = null

        val request = Request.Builder()
            .url("wss://api.upbit.com/websocket/v1")
            .build()

        val listener = object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                SLOG.D("Quotation WS OPEN")
                trySend(UpbitWsEvent.Open(response.code))

                val subscribeJson = buildSubscribeJson(
                    markets = markets,
                    unitMinutes = unitMinutes,
                    subscribeCandle = subscribeCandle,
                    subscribeTrade = subscribeTrade
                )
                webSocket.send(subscribeJson)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                val text = bytes.utf8()
                // 너무 길면 로그 폭발하니까 필요하면 축약해도 됨
                SLOG.D("Quotation WS $text")
                trySend(UpbitWsEvent.Message(text))
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                SLOG.D("Quotation WS $text")
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
    ): String {
        val ticket = UUID.randomUUID().toString()
        val codesJson = markets.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }

        val parts = mutableListOf<String>()
        parts += """{"ticket":"$ticket"}"""

        if (subscribeCandle) {
            val candleType = "candle.${unitMinutes}m"
            parts += """{"type":"$candleType","codes":$codesJson}"""
        }

        if (subscribeTrade) {
            parts += """{"type":"trade","codes":$codesJson}"""
        }

        // 배열 형태로 보내야 함
        return parts.joinToString(prefix = "[", postfix = "]", separator = ",")
    }
}
