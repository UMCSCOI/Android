package com.stable.scoi.presentation.ui.transfer.bottomsheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentNetworkBottomsheetBinding

class NetworkBottomSheet: BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNetworkBottomsheetBinding
    private lateinit var setNetworkType: SetNetworkType

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(
            requireContext(),
            R.style.TransparentBottomSheetDialog
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setNetworkType = parentFragment as SetNetworkType
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNetworkBottomsheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            bottomsheetNetworkTronLl.setOnClickListener {
                setNetworkType.networkType(Network.TRON)
                dismiss()
            }
            bottomsheetNetworkEthereumLl.setOnClickListener {
                setNetworkType.networkType(Network.ETHEREUM)
                dismiss()
            }
            bottomsheetNetworkKaiaLl.setOnClickListener {
                setNetworkType.networkType(Network.KAIA)
                dismiss()
            }
            bottomsheetNetworkAptosLl.setOnClickListener {
                setNetworkType.networkType(Network.APTOS)
                dismiss()
            }
            bottomsheetNetworkCloseIv.setOnClickListener {
                dismiss()
            }
        }
    }
}

enum class Network {
    TRON, ETHEREUM, KAIA, APTOS
}