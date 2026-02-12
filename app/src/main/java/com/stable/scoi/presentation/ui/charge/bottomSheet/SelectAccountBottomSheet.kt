package com.stable.scoi.presentation.ui.charge.bottomSheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stable.scoi.databinding.BottomSheetSelectAccountBinding
import com.stable.scoi.domain.model.enums.AccountType

class SelectAccountBottomSheet(
    private val onClickItem: (AccountType) -> Unit = {},
): BottomSheetDialogFragment() {
    private val binding: BottomSheetSelectAccountBinding by lazy {
        BottomSheetSelectAccountBinding.inflate(layoutInflater)
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
            textUpbit.setOnClickListener {
                onClickItem(AccountType.UPBIT)
                dismiss()
            }

            textBitsum.setOnClickListener {
                onClickItem(AccountType.BITSUM)
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}