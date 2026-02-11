package com.stable.scoi.presentation.ui.charge.bottomSheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stable.scoi.databinding.BottomSheetExceedCountBinding
import com.stable.scoi.databinding.BottomSheetLackMoneyBinding
import java.text.DecimalFormat

class ExceedBottomSheet: BottomSheetDialogFragment() {

    private val binding: BottomSheetExceedCountBinding by lazy {
        BottomSheetExceedCountBinding.inflate(layoutInflater)
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
            buttonRetry.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}