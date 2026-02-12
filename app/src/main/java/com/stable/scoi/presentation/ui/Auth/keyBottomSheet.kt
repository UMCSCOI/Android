package com.stable.scoi.presentation.ui.Auth

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentExchangeBottomsheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangeBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentExchangeBottomsheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExchangeBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomsheetExchangeBithumbLl.setOnClickListener {
            sendResult("빗썸")
        }

        binding.bottomsheetExchangeUpbitLl.setOnClickListener {
            sendResult("업비트")
        }

    }

    private fun sendResult(exchangeName: String) {
        setFragmentResult("exchangeKey", bundleOf("selectedExchange" to exchangeName))
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}