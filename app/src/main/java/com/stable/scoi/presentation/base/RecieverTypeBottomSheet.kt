package com.stable.scoi.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stable.scoi.databinding.FragmentRecieverTypeBottomsheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceiverTypeBottomSheet : BottomSheetDialogFragment() {
    lateinit var binding : FragmentRecieverTypeBottomsheetBinding
    private val viewModel : TransferViewModel by activityViewModels()
    private lateinit var setReceiverType: SetReceiverType

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecieverTypeBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setReceiverType = parentFragment as SetReceiverType
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomsheetRecievertypeIndividualLl.setOnClickListener {
            setReceiverType.individual()
            dismiss()
        }

        binding.bottomsheetRecievertypeCorporationLl.setOnClickListener {
            setReceiverType.corporation()
            dismiss()
        }

        binding.bottomsheetRecievertypeCloseIv.setOnClickListener {
            dismiss()
        }
    }
}