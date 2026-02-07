package com.stable.scoi.presentation.ui.wallet.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stable.scoi.databinding.FragmentExchangeBottomsheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangeBottomSheet : BottomSheetDialogFragment() {

    lateinit var binding: FragmentExchangeBottomsheetBinding
    private lateinit var setExchange: SetExchangeType

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

        binding.bottomsheetExchangeCloseIv.setOnClickListener {
            setExchange.empty()
            dismiss()
        }
    }
}