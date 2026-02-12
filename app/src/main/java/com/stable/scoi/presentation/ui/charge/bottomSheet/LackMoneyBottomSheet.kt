package com.stable.scoi.presentation.ui.charge.bottomSheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stable.scoi.databinding.BottomSheetLackMoneyBinding
import java.text.DecimalFormat

class LackMoneyBottomSheet(
    private val money: String = "",
    private val onClickFill: () -> Unit = {},
): BottomSheetDialogFragment() {

    private val binding: BottomSheetLackMoneyBinding by lazy {
        BottomSheetLackMoneyBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val amount = money.toLongOrNull() ?: 0L
            val formattedMoney = DecimalFormat("#,###").format(amount)
            textAmount.text = "${formattedMoney}원"

            imageClose.setOnClickListener {
                dismiss()
            }

            buttonClose.setOnClickListener {
                dismiss()
            }

            buttonFill.setOnClickListener {
                onClickFill()
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}