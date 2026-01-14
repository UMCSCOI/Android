package com.stable.scoi.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stable.scoi.databinding.FragmentExchangeBottomsheetBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class ExchangeBottomSheet : BottomSheetDialogFragment() {

    lateinit var binding: FragmentExchangeBottomsheetBinding

    private val viewModel: TransferViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExchangeBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomsheetExchangeUpbitLl.setOnClickListener {
            viewModel.setExchangeUpbit()
            viewModel.eventCancel()
            dismiss()
        }

        binding.bottomsheetExchangeBithumbLl.setOnClickListener {
            viewModel.setExchangeBithumb()
            viewModel.eventCancel()
            dismiss()
        }

        binding.bottomsheetExchangeBinanceLl.setOnClickListener {
            viewModel.setExchangeBinance()
            viewModel.eventCancel()
            dismiss()
        }

        binding.bottomsheetExchangeCloseIv.setOnClickListener {
            viewModel.eventCancel()
            dismiss()
        }
    }
}