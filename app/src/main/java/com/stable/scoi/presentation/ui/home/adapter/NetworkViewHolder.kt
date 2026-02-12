package com.stable.scoi.presentation.ui.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.databinding.ItemNetworkBinding
import com.stable.scoi.domain.model.home.Network
import com.stable.scoi.extension.gone
import com.stable.scoi.extension.visible

class NetworkViewHolder(
    private val binding: ItemNetworkBinding,
    private val listener: NetworkAdapter.Delegate
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Network) {
        binding.apply {
            if (item.isChecked) imageCheck.visible() else imageCheck.gone()
            textName.text = item.name
            textName.setOnClickListener {
                listener.onClickItem(item)
            }
        }
    }
}