package com.stable.scoi.presentation.ui.wallet.recyclerview.transferList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.databinding.ItemMywalletListBinding
import com.stable.scoi.domain.model.wallet.Transactions
import com.stable.scoi.domain.model.wallet.TransactionsCharge
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.Locale

class RecentTransferListRVAdapter(private val recentTransferListOnClickListener: RecentTransferListOnClickListener): RecyclerView.Adapter<RecentTransferListRVAdapter.ViewHolder>() {

    private val items = ArrayList<Transactions>()

    fun updateItems(newItems: List<Transactions>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemMywalletListBinding = ItemMywalletListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.binding.WalletListVIEW.setOnClickListener {
            recentTransferListOnClickListener.RTLOnClickListener(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: ItemMywalletListBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(recentTransferList: Transactions) {
            binding.apply {

                val sign = when (recentTransferList.type) {
                    "WITHDRAW" -> "-"
                    "DEPOSIT" -> "+"
                    else -> ""
                }

                val type = when (recentTransferList.type) {
                    "WITHDRAW" -> "출금"
                    "DEPOSIT" -> "입금"
                    else -> ""
                }

                WalletListTimeTV.text = formatDate(recentTransferList.createdAt)
                WalletListAssetSymbolTV.text = recentTransferList.currency
                WalletListAmountTV.text = "$sign${recentTransferList.amount}"
                WalletListAssetSymbolTitleTV.text = recentTransferList.currency
                WalletListTitleAssetSymbolTV.text = recentTransferList.currency
                WalletListStateTV.text = type
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