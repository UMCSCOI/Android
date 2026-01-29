package com.stable.scoi.presentation.ui.charge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.databinding.ItemRecentAmountBinding
import com.stable.scoi.domain.model.RecentTrade

class ChargeRecentTradeAdapter(
) : ListAdapter<RecentTrade, RecyclerView.ViewHolder>(
    NoticeImageDiffCallBack()
) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is RecentTradeViewHolder -> holder.bind(currentList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemRecentAmountBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecentTradeViewHolder(binding)
    }
}

class NoticeImageDiffCallBack : DiffUtil.ItemCallback<RecentTrade>() {
    override fun areContentsTheSame(
        oldItem: RecentTrade,
        newItem: RecentTrade
    ): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: RecentTrade,
        newItem: RecentTrade
    ): Boolean {
        return oldItem == newItem
    }
}