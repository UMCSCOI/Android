package com.stable.scoi.presentation.ui.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.R
import com.stable.scoi.databinding.ItemAccountCardBinding
import com.stable.scoi.domain.model.enums.AccountType
import com.stable.scoi.domain.model.home.AccountCard
import com.stable.scoi.extension.gone
import com.stable.scoi.extension.visible

class AccountCardViewHolder(
    private val binding: ItemAccountCardBinding,
    private val listener: AccountCardAdapter.Delegate
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AccountCard) {
        binding.apply {
            root.setOnClickListener {
                if (item.isEmpty) listener.onClickCard()
            }

            when (item.type) {
                AccountType.BITSUM -> {
                    textAccountTitle.text = "빗썸"
                    imageAccount.setImageResource(R.drawable.ic_bitsum)
                }
                AccountType.UPBIT -> {
                    textAccountTitle.text = "업비트"
                    imageAccount.setImageResource(R.drawable.ic_upbit)
                }
            }

            if (item.isEmpty) {
                imageEmptyAccount.visible()
                imagePlus.visible()
                textEmptyAccount.visible()

                imageUsdc.gone()
                imageUsdt.gone()
                textUsdc.gone()
                textUsdt.gone()
                textUsdcMoney.gone()
                textUsdtMoney.gone()
            } else {
                imageEmptyAccount.gone()
                imagePlus.gone()
                textEmptyAccount.gone()

                imageUsdc.visible()
                imageUsdt.visible()
                textUsdc.visible()
                textUsdt.visible()
                textUsdcMoney.visible()
                textUsdtMoney.visible()
            }

            textUsdcMoney.text = item.usdc
            textUsdtMoney.text = item.usdt
        }
    }
}