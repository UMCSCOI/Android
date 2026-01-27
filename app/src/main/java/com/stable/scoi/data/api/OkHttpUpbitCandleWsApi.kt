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
     * unitMinutes: 1, 3, 5, 10, 15, 30, 60, 240 ...
     * market: "KRW-BTC"
     */
    fun streamMinuteCandle(
        unitMinutes: Int,
        market: String,
    ): Flow<UpbitWsEvent> = callbackFlow {

        val request = Request.Builder()
            .url("wss://api.upbit.com/websocket/v1")
            .build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                SLOG.D("Candle WS OPEN")
                trySend(UpbitWsEvent.Open(response.code))

                val subscribeJson = buildSubscribeJson(unitMinutes, market)
                webSocket.send(subscribeJson)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // Upbit은 바이너리로 주는 경우가 흔함
                val text = bytes.utf8()
                SLOG.D("Candle WS $text")
                trySend(UpbitWsEvent.Message(text))
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                SLOG.D("Candle WS $text")
                trySend(UpbitWsEvent.Message(text))
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                SLOG.D("Candle WS CLOSING: $code / $reason")
                trySend(UpbitWsEvent.Closing(code, reason))
                webSocket.close(code, reason)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                SLOG.D("Candle WS FAIL: ${t.message}")
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

    private fun buildSubscribeJson(unitMinutes: Int, market: String): String {
        val ticket = UUID.randomUUID().toString()
        // Upbit WS candle type 예: candle.1m, candle.3m ...
        val type = "candle.${unitMinutes}m"

        return """
            [
              {"ticket":"$ticket"},
              {"type":"$type","codes":["$market"]}
            ]
        """.trimIndent()
    }
}
