package com.stable.scoi.presentation.ui.transfer.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stable.scoi.databinding.FragmentExchangeBottomsheetBinding
import com.stable.scoi.presentation.ui.transfer.TransferViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class ExchangeBottomSheet : BottomSheetDialogFragment() {

    lateinit var binding: FragmentExchangeBottomsheetBinding
    private lateinit var setExchange: SetExchangeType
    private val viewModel: TransferViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExchangeBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setExchange = parentFragment as SetExchangeType
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomsheetExchangeUpbitLl.setOnClickListener {
            setExchange.upbit()
            dismiss()
        }

        binding.bottomsheetExchangeBithumbLl.setOnClickListener {
            setExchange.bithumb()
            dismiss()
        }

        binding.bottomsheetExchangeBinanceLl.setOnClickListener {
            setExchange.binance()
            dismiss()
        }

        binding.bottomsheetExchangeCloseIv.setOnClickListener {
            setExchange.empty()
            dismiss()
        }
    }
}