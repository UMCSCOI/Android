package com.stable.scoi.presentation.ui.wallet.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentChargeCancelCompleteDialogBinding

class CancelCompleteDialogFragment: DialogFragment() {
    private lateinit var binding: FragmentChargeCancelCompleteDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChargeCancelCompleteDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.WalletChargeCancelKeepTV.setOnClickListener {
            findNavController().navigate(R.id.wallet_charge_detail_fragment)
        }

        binding.WalletChargeCancelConfirmTV.setOnClickListener {
            dismiss()
        }
    }
}