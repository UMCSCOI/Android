package com.stable.scoi.presentation.ui.wallet.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentChargeCancelDialogBinding
import com.stable.scoi.presentation.ui.wallet.WalletViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class ChargeCancelDialogFragment: DialogFragment() {
    private lateinit var binding: FragmentChargeCancelDialogBinding

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
        binding = FragmentChargeCancelDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            cancelTime.text = formatDate(viewModel.cancelData.value.createdAt)
            cancelCoinType.text = when (viewModel.cancelData.value.market) {
            "KRW-USDT" -> "USDT"
            "KRW-USDC" -> "USDC"
            else -> ""
            }
            cancelType.text = when (viewModel.cancelData.value.side) {
            "bid" -> "충전"
            "ask" -> "현금 교환"
            else -> ""
            }
            cancelAmountCoinType.text = when (viewModel.cancelData.value.market) {
                "KRW-USDT" -> "USDT"
                "KRW-USDC" -> "USDC"
                else -> ""
            }
            cancelAmount.text = viewModel.cancelData.value.volume
        }

        binding.WalletChargeCancelKeepTV.setOnClickListener {
            dismiss()
        }

        binding.WalletChargeCancelConfirmTV.setOnClickListener {
            viewModel.cancelOrder(viewModel.cancelRequest.value)
            CancelCompleteDialogFragment().show(
                parentFragmentManager,
                "dialog"
            )
            dismiss()
        }
    }

    fun formatDate(dateString: String): String {
        return try {
            val input = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ssXXX",
                Locale.getDefault()
            )
            val output = SimpleDateFormat(
                "MM.dd HH:mm:ss",
                Locale.getDefault()
            )
            val date = input.parse(dateString)
            output.format(date!!)
        } catch (e: Exception) {
            dateString
        }
    }

}