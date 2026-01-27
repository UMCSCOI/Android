package com.stable.scoi.extension

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.stable.scoi.domain.model.TvCandle
import com.stable.scoi.util.SLOG
import org.json.JSONArray
import org.json.JSONObject

@SuppressLint("SetJavaScriptEnabled")
fun WebView.setupTvChart(
    assetFileName: String = "tv_chart.html",
    onReady: (() -> Unit)? = null,
) {
    val isDebuggable =
        (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    if (isDebuggable) WebView.setWebContentsDebuggingEnabled(true)

    settings.apply {
        javaScriptEnabled = true
        domStorageEnabled = true

        // 뷰포트/스케일
        useWideViewPort = true
        loadWithOverviewMode = true

        // 줌 필요하면 true로
        setSupportZoom(false)
        builtInZoomControls = false
        displayZoomControls = false

        // 캐시
        cacheMode = WebSettings.LOAD_DEFAULT

        // 만약 http 리소스를 섞어 쓴다면 필요(보통은 안 씀)
        mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

        // assets 로드용
        allowFileAccess = true
        allowContentAccess = true
    }

    webChromeClient = object : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
            // tv_chart.html에서 console.log 찍으면 여기로 들어옴
            SLOG.D("WebViewConsole: ${consoleMessage.message()} (${consoleMessage.sourceId()}:${consoleMessage.lineNumber()})")
            return super.onConsoleMessage(consoleMessage)
        }
    }

    webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            onReady?.invoke()
        }
    }

    loadUrl("file:///android_asset/$assetFileName")
}

fun WebView.tvSetData(candles: List<TvCandle>) {
    val candleArr = JSONArray()
    val volumeArr = JSONArray()

    candles.forEach { c ->
        candleArr.put(
            JSONObject()
                .put("time", c.time)
                .put("open", c.open)
                .put("high", c.high)
                .put("low", c.low)
                .put("close", c.close)
        )

        volumeArr.put(
            JSONObject()
                .put("time", c.time)
                .put("value", c.volume)
                .put("color", c.volumeColor)
        )
    }

    val js = "window.tvChart && window.tvChart.setData(${candleArr}, ${volumeArr});"
    evaluateJavascript(js, null)
}

fun WebView.tvUpdate(c: TvCandle) {
    val candleObj = JSONObject()
        .put("time", c.time)
        .put("open", c.open)
        .put("high", c.high)
        .put("low", c.low)
        .put("close", c.close)

    val volumeObj = JSONObject()
        .put("time", c.time)
        .put("value", c.volume)
        .put("color", c.volumeColor)

    val js = "window.tvChart && window.tvChart.update(${candleObj}, ${volumeObj});"
    evaluateJavascript(js, null)
}