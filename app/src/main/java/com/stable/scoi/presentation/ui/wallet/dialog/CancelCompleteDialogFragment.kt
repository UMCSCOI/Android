package com.stable.scoi.presentation.ui.wallet.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentChargeCancelCompleteDialogBinding
import com.stable.scoi.presentation.ui.wallet.WalletViewModel
class CancelCompleteDialogFragment: DialogFragment() {
    private lateinit var binding: FragmentChargeCancelCompleteDialogBinding

    private val viewModel: WalletViewModel by viewModels (
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentDialog)
    }

    override fun onStart() {
        super.onStart()

        val margin = resources.displayMetrics.density * 16

        dialog?.window?.apply {
            setLayout(
                (resources.displayMetrics.widthPixels - margin * 2).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

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

        val currency = when (viewModel.cancelData.value.market) {
            "KRW-USDT" -> "USDT"
            "KRW-USDC" -> "USDC"
            else -> ""
        }

        binding.WalletChargeCancelKeepTV.setOnClickListener {
            viewModel.transactionsDetail(
                viewModel.exType,
                "TOPUP",
                null,
                viewModel.cancelData.value.uuid,
                currency
            )
            findNavController().navigate(R.id.wallet_charge_detail_fragment)
        }

        binding.WalletChargeCancelConfirmTV.setOnClickListener {
            dismiss()
        }
    }
}