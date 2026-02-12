package com.stable.scoi.presentation.ui.charge.bottomSheet

import android.content.DialogInterface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stable.scoi.R
import com.stable.scoi.databinding.BottomSheetChargeBinding
import com.stable.scoi.domain.model.enums.ChargePageType
import java.text.DecimalFormat

class ChargeBottomSheet(
    private val money: String = "",
    private val count: String = "",
    private val coin: String = "",
    private val total: String = "",
    private val type: ChargePageType = ChargePageType.CHARGE,
    private val onClickRight: () -> Unit = {},
): BottomSheetDialogFragment() {

    private val binding: BottomSheetChargeBinding by lazy {
        BottomSheetChargeBinding.inflate(layoutInflater)
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
            val highlightText = "$count $coin"
            val actionText = if (type == ChargePageType.CHARGE) "를\n충전할까요?" else "를\n교환할까요?"

            val builder = SpannableStringBuilder()

            val colorSpan = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.active))

            builder.append(highlightText)
            builder.setSpan(
                colorSpan,
                0,
                highlightText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            builder.append(actionText)

            textChargeTitle.text = builder


            val decimalFormat = DecimalFormat("#,###")

            val moneyVal = money.replace("[^0-9]".toRegex(), "").toLongOrNull() ?: 0L
            textUsdtMoney.text = "${decimalFormat.format(moneyVal)}원"

            val totalVal = total.replace("[^0-9]".toRegex(), "").toLongOrNull() ?: 0L
            textTotalMoney.text = "${decimalFormat.format(totalVal)}원"


            imageClose.setOnClickListener {
                dismiss()
            }

            buttonClose.setOnClickListener {
                dismiss()
            }

            buttonFill.setOnClickListener {
                onClickRight()
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}