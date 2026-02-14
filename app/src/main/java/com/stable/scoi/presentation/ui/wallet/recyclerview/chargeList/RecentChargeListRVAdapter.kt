package com.stable.scoi.presentation.ui.wallet.recyclerview.chargeList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.databinding.ItemMywalletChargeListBinding
import com.stable.scoi.domain.model.wallet.Transactions
import com.stable.scoi.domain.model.wallet.TransactionsCharge
import java.text.SimpleDateFormat
import java.util.Locale

class RecentChargeListRVAdapter(private val recentChargeListOnClickListener: RecentChargeListOnClickListener): RecyclerView.Adapter<RecentChargeListRVAdapter.ViewHolder>() {

    private val items = ArrayList<TransactionsCharge>()

    fun updateItems(newItems: List<TransactionsCharge>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemMywalletChargeListBinding = ItemMywalletChargeListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.binding.WalletListVIEW.setOnClickListener {
            recentChargeListOnClickListener.RCLOnClickListener(items[position])
        }
        holder.binding.WalletListCancelTV.setOnClickListener {
            recentChargeListOnClickListener.cancelOnclickListener(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: ItemMywalletChargeListBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(recentChargeList: TransactionsCharge) {
            binding.apply {
                val coinType = when (recentChargeList.market) {
                    "KRW-USDT" -> "USDT"
                    "KRW-USDC" -> "USDC"
                    else -> ""
                }
                WalletListTimeTV.text = formatDate(recentChargeList.createdAt)
                WalletListAssetSymbolTV.text = coinType
                WalletListChargeAssetSymbolTV.text = coinType

                val sign = when (recentChargeList.side) {
                    "bid" -> "+"
                    "ask" -> "-"
                    else -> ""
                }

                val side = when (recentChargeList.side) {
                    "bid" -> "충전"
                    "ask" -> "현금 교환"
                    else -> ""
                }

                val state = when (recentChargeList.state) {
                    "done" -> "완료"
                    "wait" -> "대기"
                    "canceled" -> "취소"
                    else -> ""
                }

                val amount = sign + recentChargeList.executedVolume

                WalletListChargeStateTV.text = "$side $state"
                WalletListAmountTV.text = amount
                WalletListAssetSymbolTitleTV.text = coinType
               // WalletListTotalAssetSymbolTV.text = coinType

                if (recentChargeList.state != "wait") {
                    WalletListCancelTV.visibility = View.GONE
                }
            }
        }
    }

    fun formatDate(dateString: String): String {
        return try {
            val input = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ssXXX",
                Locale.getDefault()
            )
            val output = SimpleDateFormat(
                "MM.dd HH:mm:ss",
                Locale.getDefault()
            )
            val date = input.parse(dateString)
            output.format(date!!)
        } catch (e: Exception) {
            dateString
        }
    }
}