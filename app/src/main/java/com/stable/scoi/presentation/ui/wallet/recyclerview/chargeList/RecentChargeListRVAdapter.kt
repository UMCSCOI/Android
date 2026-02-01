package com.stable.scoi.presentation.ui.wallet.recyclerview.chargeList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.databinding.ItemMywalletListBinding

class RecentChargeListRVAdapter(private val recentChargeList: ArrayList<RecentChargeList>, private val recentChargeListOnClickListener: RecentChargeListOnClickListener): RecyclerView.Adapter<RecentChargeListRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemMywalletListBinding = ItemMywalletListBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recentChargeList[position])
        holder.binding.WalletListVIEW.setOnClickListener {
            recentChargeListOnClickListener.RCLOnClickListener(recentChargeList[position])
        }
    }

    override fun getItemCount(): Int = recentChargeList.size

    inner class ViewHolder(val binding: ItemMywalletListBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(recentChargeList: RecentChargeList) {
            binding.apply {
                WalletListTimeTV.text = recentChargeList.occurredAt//데이터 가공 필요 (날짜 시간)
                WalletListAssetSymbolTV.text = recentChargeList.assetSymbol//API 명세서 누락 항목
                WalletListNameTV.text = recentChargeList.counterparty.displayName
                WalletListAmountTV.text = recentChargeList.amount//데이터 가공 필요 (+/-)
                WalletListAssetSymbolTitleTV.text = recentChargeList.assetSymbol
                WalletListTotalAmountTV.text = recentChargeList.netAmount
                WalletListTotalAssetSymbolTV.text = recentChargeList.assetSymbol//API 명세서 누락 항목
                //추가로 상태에 대한 API 정보 또한 추가 되어야 함 -> API 명세서 수정 후 기능 구현 예정
            }
        }
    }
}