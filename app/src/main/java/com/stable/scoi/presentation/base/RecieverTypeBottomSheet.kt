package com.stable.scoi.presentation.base

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
class RecieverTypeBottomSheet : BottomSheetDialogFragment() {
    lateinit var binding : FragmentRecieverTypeBottomsheetBinding
    private val viewModel : TransferViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecieverTypeBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomsheetRecievertypeIndividualLl.setOnClickListener {
            viewModel.setRecieverTypeIndividual()
            viewModel.eventCancel()
            dismiss()
        }

        binding.bottomsheetRecievertypeCorporationLl.setOnClickListener {
            viewModel.setRecieverTypeCorporation()
            viewModel.eventCancel()
            dismiss()
        }

        binding.bottomsheetRecievertypeCloseIv.setOnClickListener {
            dismiss()
        }
    }
}