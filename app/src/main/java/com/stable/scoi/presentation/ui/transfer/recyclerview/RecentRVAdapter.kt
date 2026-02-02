package com.stable.scoi.presentation.ui.transfer.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.databinding.ItemBookmarkBinding
import com.stable.scoi.presentation.ui.transfer.Recent

class RecentRVAdapter(private val recentList: ArrayList<Recent>, private val recentOnCliCKListener: RecentOnCliCKListener): RecyclerView.Adapter<RecentRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemBookmarkBinding = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(recentList[position])
        holder.binding.bookmarkReceiverVIEW.setOnClickListener {
            recentOnCliCKListener.rcOnclickListener(recentList[position])
        }
        holder.binding.bookmarkBookmarkIV.setOnClickListener {
            //API 연동 후 추가
        }
    }

    override fun getItemCount(): Int = recentList.size

    inner class ViewHolder(val binding: ItemBookmarkBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(recent: Recent) {
            binding.apply {
                bookmarkReceiverNameTV.text = recent.recipientName
                bookmarkReceiverAddressTV.text = recent.walletAddress
                //거래소 이미지 추가
            }
        }
    }
}



