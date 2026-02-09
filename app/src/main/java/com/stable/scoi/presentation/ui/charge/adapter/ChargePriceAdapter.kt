package com.stable.scoi.presentation.ui.charge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.databinding.ItemRecentAnalyzeBinding
import com.stable.scoi.extension.gone
import com.stable.scoi.extension.visible
import java.text.NumberFormat
import java.util.Locale

// 호가창용 데이터 모델
data class PriceItem(
    val price: Double,
    val isCurrentPrice: Boolean,  // 현재가 여부 (점, 텍스트 표시용)
    val prevClosingPrice: Double  // 전일 종가 (색상 결정용)
)

class ChargePriceAdapter : ListAdapter<PriceItem, ChargePriceAdapter.PriceViewHolder>(
    PriceDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val binding = ItemRecentAnalyzeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PriceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class PriceViewHolder(private val binding: ItemRecentAnalyzeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PriceItem) {

            val nf = NumberFormat.getNumberInstance(Locale.KOREA)
            binding.textMoney.text = nf.format(item.price)

            val color = when {
                item.price > item.prevClosingPrice -> "#EF2B2A".toColorInt() // 상승 (빨강)
                item.price < item.prevClosingPrice -> "#4A4AFA".toColorInt() // 하락 (파랑)
                else -> "#111111".toColorInt()
            }
            binding.textMoney.setTextColor(color)

            if (item.isCurrentPrice) {
                binding.textNowMoney.visible()

            } else {
                binding.textNowMoney.gone()
            }
        }
    }
}

class PriceDiffCallback : DiffUtil.ItemCallback<PriceItem>() {
    override fun areItemsTheSame(oldItem: PriceItem, newItem: PriceItem): Boolean {
        return oldItem.price == newItem.price
    }

    override fun areContentsTheSame(oldItem: PriceItem, newItem: PriceItem): Boolean {
        return oldItem == newItem
    }
}