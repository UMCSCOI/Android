package com.stable.scoi.presentation.ui.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.R
import com.stable.scoi.databinding.ItemAccountCardBinding
import com.stable.scoi.domain.model.enums.AccountType
import com.stable.scoi.domain.model.home.AccountCard

class AccountCardViewHolder(
    private val binding: ItemAccountCardBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AccountCard) {
        binding.apply {
            when (item.type) {
                AccountType.BITSUM -> {
                    textAccountTitle.text = "빗썸"
                    imageAccount.setImageResource(R.drawable.ic_bitsum)
                }
                AccountType.UPBIT -> {
                    textAccountTitle.text = "업비트"
                    imageAccount.setImageResource(R.drawable.ic_upbit)
                }
                AccountType.BINANCE -> {
                    textAccountTitle.text = "BINANCE"
                    imageAccount.setImageResource(R.drawable.ic_binance)
                }
            }

            textUsdcMoney.text = item.usdc
            textUsdtMoney.text = item.usdt
        }
    }
}