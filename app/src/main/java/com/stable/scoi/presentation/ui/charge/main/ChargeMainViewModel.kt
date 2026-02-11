package com.stable.scoi.presentation.ui.charge.main

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.stable.scoi.domain.model.CandleStreamEvent
import com.stable.scoi.domain.model.UpbitTicker
import com.stable.scoi.domain.model.enums.ChargeInputType
import com.stable.scoi.domain.model.enums.ChargePageType
import com.stable.scoi.domain.repository.ChartRepository
import com.stable.scoi.presentation.base.BaseViewModel
import com.stable.scoi.presentation.base.UiEvent
import com.stable.scoi.presentation.base.UiState
import com.stable.scoi.util.Format.formatWon
import com.stable.scoi.util.Format.unformatWon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.graphics.toColorInt
import com.stable.scoi.domain.model.enums.AccountType
import com.stable.scoi.domain.model.home.AccountCard
import com.stable.scoi.domain.repository.ChargeRepository
import kotlinx.coroutines.Job

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ChargeMainViewModel @Inject constructor(
    private val chartRepository: ChartRepository,
    private val chargeRepository: ChargeRepository,
) : BaseViewModel<ChargeMainUiState, ChargeMainEvent>(
    ChargeMainUiState(),
) {

    init {
        getBithumb()
    }

    private var coinJob: Job? = null

    fun startCoinMonitoring() {
        // 이미 연결 중이면 중복 실행 방지
        if (coinJob?.isActive == true) return

        coinJob = viewModelScope.launch {
            chartRepository.streamUnified(
                chartMarket = null,
                tickerMarkets = listOf("KRW-USDT", "KRW-USDC")
            )
                .collect { ev ->
                    if (ev is CandleStreamEvent.TickerUpdate) {
                        when (ev.ticker.market) {
                            "KRW-USDT" -> updateTickerState(ev.ticker, true)
                            "KRW-USDC" -> updateTickerState(ev.ticker, false)
                        }
                    }
                }
        }
    }

    fun onClickMyPage() {
        //
    }

    fun getBithumb() = viewModelScope.launch {
        resultResponse(
            response = chargeRepository.getMyBalances("Bithumb"),
            successCallback = {
                updateState {
                    copy(
                        myKrwMoney = it.balances.find { it.currency == "KRW" }!!.balance,
                        myUsdcMoney = it.balances.find { it.currency == "USDC" }!!.balance,
                        myUsdtMoney = it.balances.find { it.currency == "USDT" }!!.balance,
                    )
                }
            }
        )
    }

    fun getUpbit() {
        viewModelScope.launch {
            resultResponse(
                response = chargeRepository.getMyBalances("Upbit"),
                successCallback = {
                    updateState {
                        copy(
                            myKrwMoney = it.balances.find { it.currency == "KRW" }!!.balance,
                            myUsdcMoney = it.balances.find { it.currency == "USDC" }!!.balance,
                            myUsdtMoney = it.balances.find { it.currency == "USDT" }!!.balance,
                        )
                    }
                }
            )
        }
    }

    fun stopCoinMonitoring() {
        coinJob?.cancel() // Job을 취소하면 awaitClose가 실행되어 소켓이 닫힘
        coinJob = null
    }

    private fun updateTickerState(ticker: UpbitTicker, isUsdt: Boolean) {
        val formattedPrice = formatPrice(ticker.tradePrice)
        val formattedRate = formatRate(ticker.signedChangeRate)
        val color = getColorByRate(ticker.signedChangeRate)

        val info = CoinInfo(
            price = formattedPrice,
            rate = formattedRate,
            color = color,
            rawPrice = ticker.tradePrice
        )

        updateState {
            if (isUsdt) {
                copy(usdtInfo = info)
            } else {
                copy(usdcInfo = info)
            }
        }
    }

    private fun formatPrice(price: Double): String {
        return if (price >= 100) String.format("%,.0f", price)
        else String.format("%,.2f", price)
    }

    private fun formatRate(rate: Double): String {
        val percent = rate * 100
        return if (percent > 0) String.format("+%.2f%%", percent)
        else String.format("%.2f%%", percent)
    }

    private fun getColorByRate(rate: Double): Int {
        return when {
            rate > 0 -> "#EF2B2A".toColorInt()
            rate < 0 -> "#4A4AFA".toColorInt()
            else -> "#767676".toColorInt()
        }
    }
}


data class ChargeMainUiState(
    val myKrwMoney: String = "",
    val myUsdtMoney: String = "",
    val myUsdcMoney: String = "",
    val usdtInfo: CoinInfo = CoinInfo(),
    val usdcInfo: CoinInfo = CoinInfo()
) : UiState

data class CoinInfo(
    val price: String = "-",      // 화면 표시용 (예: "1,450")
    val rate: String = "0.00%",   // 화면 표시용 (예: "+0.5%")
    val color: Int = Color.BLACK, // 색상
    val rawPrice: Double = 0.0    // 계산용 실제 가격
)

sealed class ChargeMainEvent : UiEvent {
    // 필요 시 이벤트 정의
}