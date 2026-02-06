package com.stable.scoi.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.databinding.ItemNetworkBinding
import com.stable.scoi.domain.model.home.Network

class NetworkAdapter(
    private val listener : Delegate
) : ListAdapter<Network, RecyclerView.ViewHolder>(
    NetworkDiffCallBack()
) {

    interface Delegate {
        fun onClickItem(item: Network)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is NetworkViewHolder -> holder.bind(currentList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemNetworkBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NetworkViewHolder(binding, listener)
    }
}

class NetworkDiffCallBack : DiffUtil.ItemCallback<Network>() {
    override fun areContentsTheSame(
        oldItem: Network,
        newItem: Network
    ): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: Network,
        newItem: Network
    ): Boolean {
        return oldItem.name == newItem.name
    }
}