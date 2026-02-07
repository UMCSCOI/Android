package com.stable.scoi.presentation.ui.wallet.recyclerview.transferList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.databinding.ItemMywalletListBinding

class RecentTransferListRVAdapter(private val recentTransferList: ArrayList<RecentTransferList>, private val recentTransferListOnClickListener: RecentTransferListOnClickListener): RecyclerView.Adapter<RecentTransferListRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemMywalletListBinding = ItemMywalletListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recentTransferList[position])
        holder.binding.WalletListVIEW.setOnClickListener {
            recentTransferListOnClickListener.RTLOnClickListener(recentTransferList[position])
        }
    }

    override fun getItemCount(): Int = recentTransferList.size

    inner class ViewHolder(val binding: ItemMywalletListBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(recentTransferList: RecentTransferList) {
            binding.apply {
                WalletListTimeTV.text = recentTransferList.occurredAt//데이터 가공 필요 (날짜 시간)
                WalletListAssetSymbolTV.text = recentTransferList.assetSymbol//API 명세서 누락 항목
                WalletListNameTV.text = recentTransferList.counterparty.displayName
                WalletListAmountTV.text = recentTransferList.amount//데이터 가공 필요 (+/-)
                WalletListAssetSymbolTitleTV.text = recentTransferList.assetSymbol
                WalletListTotalAmountTV.text = recentTransferList.netAmount
                WalletListTotalAssetSymbolTV.text = recentTransferList.assetSymbol//API 명세서 누락 항목
            }
        }
    }
}