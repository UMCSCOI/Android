package com.stable.scoi.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.databinding.ItemAccountCardBinding
import com.stable.scoi.databinding.ItemRecentAmountBinding
import com.stable.scoi.domain.model.home.AccountCard

class AccountCardAdapter(
) : ListAdapter<AccountCard, RecyclerView.ViewHolder>(
    NoticeImageDiffCallBack()
) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is AccountCardViewHolder -> holder.bind(currentList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemAccountCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AccountCardViewHolder(binding)
    }
}

class NoticeImageDiffCallBack : DiffUtil.ItemCallback<AccountCard>() {
    override fun areContentsTheSame(
        oldItem: AccountCard,
        newItem: AccountCard
    ): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: AccountCard,
        newItem: AccountCard
    ): Boolean {
        return oldItem.key == newItem.key
    }
}