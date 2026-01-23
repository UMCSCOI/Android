package com.stable.scoi.presentation.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stable.scoi.databinding.FragmentAssetSymbolBottomsheetBinding
import com.stable.scoi.databinding.FragmentExchangeBottomsheetBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class AssetSymbolBottomSheet: BottomSheetDialogFragment() {
    lateinit var binding: FragmentAssetSymbolBottomsheetBinding
    private lateinit var setAssetSymbol: SetAssetSymbol

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAssetSymbolBottomsheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setAssetSymbol = parentFragment as SetAssetSymbol
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            bottomsheetAssetSymbolUSDTLL.setOnClickListener {
                setAssetSymbol.typeUSDT()
                dismiss()
            }

            bottomsheetAssetSymbolUSDCLL.setOnClickListener {
                setAssetSymbol.typeUSDC()
                dismiss()
            }

            bottomsheetAssetSymbolCloseIv.setOnClickListener {
                dismiss()
            }
        }
    }
}