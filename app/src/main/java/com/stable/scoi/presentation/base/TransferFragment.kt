package com.stable.scoi.presentation.base

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentTransferBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransferFragment : BaseFragment<FragmentTransferBinding, TransferState, TransferEvent, TransferViewModel>(
    FragmentTransferBinding::inflate
) {
    override val viewModel: TransferViewModel by activityViewModels()

    override fun initView() {
        //input
        binding.TransferInputNameET.setOnClickListener {
            viewModel.onReceiverTypeClicked()
        }

        binding.TransferReceiverTypeTV.setOnClickListener {
            viewModel.onReceiverTypeChange()
        }

        binding.TransferInputExchangeET.isFocusable = false
        binding.TransferInputExchangeET.setOnClickListener {
            viewModel.onExchangeClicked()
        }

        binding.TransferInputNameET.text.toString()

        binding.TransferNextTV.setOnClickListener {
            val name: String = binding.TransferInputNameET.text.toString()
            val address: String = binding.TransferInputAddressET.text.toString()
            viewModel.submitReceiver(name,address)
            viewModel.onClickNextButton()
        }





        //output
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.receiverType.collect { receiverType ->
                        when (receiverType) {
                            ReceiverType.Null -> {
                                binding.TransferInputNameET.isFocusable = false
                            }
                            ReceiverType.Individual -> {
                                binding.TransferInputNameET.isFocusable = true
                                binding.TransferInputNameET.isFocusableInTouchMode = true
                                binding.TransferInputNameET.requestFocus()
                                binding.TransferReceiverTypeTV.visibility = View.VISIBLE
                                binding.TransferReceiverTypeTV.text = "개인"
                                binding.TransferCorpNameENGET.visibility = View.GONE
                                binding.TransferCorpNameENGTV.visibility = View.GONE
                                binding.TransferCorpNameKORET.visibility = View.GONE
                                binding.TransferCorpNameKORTV.visibility = View.GONE
                            }
                            ReceiverType.Corporation -> {
                                binding.TransferInputNameET.isFocusable = true
                                binding.TransferInputNameET.isFocusableInTouchMode = true
                                binding.TransferInputNameET.requestFocus()
                                binding.TransferReceiverTypeTV.visibility = View.VISIBLE
                                binding.TransferReceiverTypeTV.text = "법인"
                                binding.TransferCorpNameENGET.visibility = View.VISIBLE
                                binding.TransferCorpNameENGTV.visibility = View.VISIBLE
                                binding.TransferCorpNameKORET.visibility = View.VISIBLE
                                binding.TransferCorpNameKORTV.visibility = View.VISIBLE
                            }
                        }
                    }
                }

                launch {
                    viewModel.receiver.collect { receiver ->
                        if (receiver.receiverName == "") {
                            binding.TransferInputNameWarningTV.visibility = View.VISIBLE
                        }
                        else {
                            binding.TransferInputNameWarningTV.visibility = View.GONE
                        }

                        if (receiver.receiverAddress == "") {
                            binding.TransferInputAddressWarningTV.visibility = View.VISIBLE
                        }
                        else {
                            binding.TransferInputAddressWarningTV.visibility = View.GONE
                        }
                    }
                }
            }
        }

        viewModel.nextEvent.observe(viewLifecycleOwner) { nextEvent ->
            when (nextEvent) {
                TransferEvent.Submit -> {
                    findNavController().navigate(R.id.transfer_amount_fragment)
                }
                else -> Unit
            }
        }

        viewModel.receiverTypeEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                TransferEvent.Submit -> {
                    RecieverTypeBottomSheet().show(
                        parentFragmentManager,
                        "BottomSheet"
                    )
                }
                else -> Unit
            }
        }

        viewModel.exchangeEvent.observe(viewLifecycleOwner) { exchangeEvent ->
            when (exchangeEvent) {
                TransferEvent.Submit -> {
                    ExchangeBottomSheet().show(
                        parentFragmentManager,
                        "BottomSheet"
                    )
                }
                else -> Unit
            }
        }

        viewModel.exchangeType.observe(viewLifecycleOwner) { exchange ->
            when (exchange) {
                Exchange.Upbit -> {
                    binding.TransferInputExchangeWarningTV.visibility = View.GONE
                    binding.TransferInputExchangeET.setText("업비트")
                    binding.TransferInputExchangeET.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.black)
                    )
                }
                Exchange.Bithumb -> {
                    binding.TransferInputExchangeWarningTV.visibility = View.GONE
                    binding.TransferInputExchangeET.setText("빗썸")
                    binding.TransferInputExchangeET.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.black)
                    )
                }
                Exchange.Binance -> {
                    binding.TransferInputExchangeWarningTV.visibility = View.GONE
                    binding.TransferInputExchangeET.setText("Binance")
                    binding.TransferInputExchangeET.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.black)
                    )
                }
                Exchange.Unselected -> {
                    binding.TransferInputExchangeWarningTV.visibility = View.VISIBLE
                }
                else -> Unit
            }
        }
    }
}