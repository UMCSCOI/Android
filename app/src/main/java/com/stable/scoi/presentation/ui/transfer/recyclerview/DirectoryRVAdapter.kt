package com.stable.scoi.presentation.ui.transfer.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.R
import com.stable.scoi.databinding.ItemDirectoryBinding
import com.stable.scoi.presentation.ui.transfer.Directory

class DirectoryRVAdapter(private val directoryList: ArrayList<Directory>, private val directoryOnClickListener: DirectoryOnClickListener): RecyclerView.Adapter<DirectoryRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemDirectoryBinding = ItemDirectoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(directoryList[position])
        holder.binding.directoryReceiverVIEW.setOnClickListener {
            directoryOnClickListener.dtOnclickListener(directoryList[position])
        }
    }

    override fun getItemCount(): Int = directoryList.size

    inner class ViewHolder(val binding: ItemDirectoryBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(directory: Directory) {
            binding.apply {
                directoryReceiverNameTV.text = directory.recipientKORName
                directoryReceiverAddressTV.text = directory.walletAddress
                directoryExchangeLogoIV.setImageResource(R.drawable.upbit_logo)
            }
        }
    }


}