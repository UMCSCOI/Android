package com.stable.scoi.presentation.ui.charge.adapter

import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.databinding.ItemRecentAmountBinding
import com.stable.scoi.domain.model.RecentTrade
import androidx.core.graphics.toColorInt
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToLong

class RecentTradeViewHolder(
    private val binding: ItemRecentAmountBinding,
    private val onClickItem: (RecentTrade) -> Unit = {},
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(trade: RecentTrade) {
        binding.apply {
            root.setOnClickListener {
                onClickItem(trade)
            }
            textTime.text = formatKstTime(trade.timestampMs)

            textMoney.text = formatPrice(trade.price)

            textAmount.text = formatSignedAmount(trade.volume, trade.askBid)

            val c = trade.color.toColorInt()
            textMoney.setTextColor(c)
        }
    }

    private fun formatPrice(price: Double): String {
        val nf = NumberFormat.getNumberInstance(Locale.KOREA).apply {
            maximumFractionDigits = 0
            isGroupingUsed = true
        }
        return nf.format(price)
    }

    private fun formatSignedAmount(volume: Double, askBid: String): String {
        val sign = if (askBid == "BID") "+" else "-"
        val intValue = volume.roundToLong()
        return sign + intValue.toString()
    }

    private fun formatKstTime(timestampMs: Long): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val time = Instant.ofEpochMilli(timestampMs)
                .atZone(ZoneId.of("Asia/Seoul"))
                .toLocalTime()
            time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        } else {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.KOREA).apply {
                timeZone = TimeZone.getTimeZone("Asia/Seoul")
            }
            sdf.format(Date(timestampMs))
        }
    }
}