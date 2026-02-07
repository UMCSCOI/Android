package com.stable.scoi.presentation.ui.transfer.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentSendcheckBottomsheetBinding
import com.stable.scoi.presentation.ui.transfer.TransferViewModel

class SendCheckBottomSheet: BottomSheetDialogFragment() {
    lateinit var binding : FragmentSendcheckBottomsheetBinding
    private val viewModel : TransferViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(
            requireContext(),
            R.style.TransparentBottomSheetDialog
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSendcheckBottomsheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomsheetSendCheckReceiverNameTV.text = viewModel.receiver.value.receiverKORName.toString()
        binding.bottomsheetSendCheckAssetSymbolTV.text = viewModel.information.value.assetSymbol
        binding.bottomsheetSendCheckAmountTV.text = viewModel.information.value.amount

        val myWallet = viewModel.exchangeToString(viewModel.exchangeType.value) + ' ' + viewModel.receiver.value.receiverAddress.toString()
        binding.bottomsheetSendCheckMyWalletTV.text = myWallet

        binding.bottomsheetSendCheckNetworkTV.text = viewModel.networkToString(viewModel.netWorkType.value)




        binding.bottomsheetSendCheckCloseIv.setOnClickListener {
            dismiss()
        }

        binding.BottomsheetSendCheckCancelTV.setOnClickListener {
            dismiss()
        }

        binding.BottomsheetSendCheckSendTV.setOnClickListener {
            findNavController().navigate(R.id.transfer_password_fragment)
            dismiss()
        }
    }
}