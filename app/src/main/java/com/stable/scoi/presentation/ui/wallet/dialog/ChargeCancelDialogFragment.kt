package com.stable.scoi.presentation.ui.wallet.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.stable.scoi.databinding.FragmentChargeCancelDialogBinding

class ChargeCancelDialogFragment: DialogFragment() {
    private lateinit var binding: FragmentChargeCancelDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChargeCancelDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.WalletChargeCancelKeepTV.setOnClickListener {
            dismiss()
        }

        binding.WalletChargeCancelConfirmTV.setOnClickListener {
            CancelCompleteDialogFragment().show(
                parentFragmentManager,
                "dialog"
            )
            dismiss()
        }
    }
}