package com.stable.scoi.presentation.ui.transfer.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.R
import com.stable.scoi.databinding.ItemBookmarkBinding
import com.stable.scoi.presentation.ui.transfer.BookMark

class BookMarkRVAdapter(private val bookmarkList: ArrayList<BookMark>, private val bookMarkOnClickListener: BookMarkOnClickListener): RecyclerView.Adapter<BookMarkRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemBookmarkBinding = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bookmarkList[position])
        holder.binding.bookmarkReceiverVIEW.setOnClickListener {
            bookMarkOnClickListener.bmOnclickListener(bookmarkList[position])
        }
        holder.binding.bookmarkBookmarkIV.setOnClickListener {
            //API 연동 후 추가
        }
    }

    override fun getItemCount(): Int = bookmarkList.size

    inner class ViewHolder(val binding: ItemBookmarkBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(bookMark: BookMark) {
            binding.apply {
                bookmarkReceiverNameTV.text = bookMark.recipientName
                bookmarkReceiverAddressTV.text = bookMark.walletAddress
                bookmarkExchangeLogoIV.setImageResource(R.drawable.upbit_logo)
            }
        }
    }


}