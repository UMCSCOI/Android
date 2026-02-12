package com.stable.scoi.presentation.ui.transfer.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.R
import com.stable.scoi.databinding.ItemDirectoryBinding
import com.stable.scoi.domain.model.transfer.DirectoryResult

class DirectoryRVAdapter(private val directoryOnClickListener: DirectoryOnClickListener): RecyclerView.Adapter<DirectoryRVAdapter.ViewHolder>() {

    private val items = ArrayList<DirectoryResult>()

    fun setItems(newList: List<DirectoryResult>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemDirectoryBinding = ItemDirectoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.binding.directoryReceiverVIEW.setOnClickListener {
            directoryOnClickListener.dtOnclickListener(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: ItemDirectoryBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(result: DirectoryResult) {
            binding.apply {
                directoryReceiverNameTV.text = result.recipientKoName
                directoryReceiverAddressTV.text = formatWalletAddress(result.walletAddress)
                directoryReceiverAssetSymbolTV.text = result.currency
                directoryReceiverNetworkTV.text = networkToString(result.netType)
                setExchangeImage(result.exchangeType)

                binding.directoryReceiverVIEW.setOnClickListener {
                    directoryOnClickListener.dtOnclickListener(result)
                }
            }
        }

        fun setExchangeImage(exchange: String) {
            when (exchange) {
                "UPBIT" -> {
                    binding.directoryExchangeLogoIV.setImageResource(R.drawable.upbit_logo)
                }
                "BITHUMB" -> {
                    binding.directoryExchangeLogoIV.setImageResource(R.drawable.bithumb_logo)
                }
            }
        }

        fun formatWalletAddress(address: String): String {
            return if (address.length > 23) {
                address.take(23) + "..."
            } else {
                address
            }
        }

        fun networkToString (network:String): String {
            when (network) {
                "TRX" -> return "트론"
                "ETH" -> return "이더리움"
                "KAIA" -> return "카이아"
                "APT" -> return "앱토스"
                else -> return ""
            }
        }
    }

}

