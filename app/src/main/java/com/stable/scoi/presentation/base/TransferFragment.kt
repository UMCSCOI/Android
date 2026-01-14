package com.stable.scoi.presentation.base

import android.view.View
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
    override val viewModel: TransferViewModel by viewModels()

    override fun initView() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recieverType.collect { recieverType ->
                    when (recieverType) {
                        RecieverType.Null -> {
                            binding.TransferInputNameET.isFocusable = false
                        }
                        RecieverType.Individual -> {
                            binding.TransferInputNameET.isFocusable = true
                            binding.TransferInputNameET.isFocusableInTouchMode = true
                            binding.TransferInputNameET.requestFocus()
                            binding.TransferRecieverTypeTV.visibility = View.VISIBLE
                            binding.TransferRecieverTypeTV.text = "개인"
                            binding.TransferCorpNameENGET.visibility = View.GONE
                            binding.TransferCorpNameENGTV.visibility = View.GONE
                            binding.TransferCorpNameKORET.visibility = View.GONE
                            binding.TransferCorpNameKORTV.visibility = View.GONE
                        }
                        RecieverType.Corporation -> {
                            binding.TransferInputNameET.isFocusable = true
                            binding.TransferInputNameET.isFocusableInTouchMode = true
                            binding.TransferInputNameET.requestFocus()
                            binding.TransferRecieverTypeTV.visibility = View.VISIBLE
                            binding.TransferRecieverTypeTV.text = "법인"
                            binding.TransferCorpNameENGET.visibility = View.VISIBLE
                            binding.TransferCorpNameENGTV.visibility = View.VISIBLE
                            binding.TransferCorpNameKORET.visibility = View.VISIBLE
                            binding.TransferCorpNameKORTV.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        binding.TransferInputNameET.setOnClickListener {
            viewModel.onRecieverTypeClicked()
        }

        binding.TransferRecieverTypeTV.setOnClickListener {
            viewModel.onRecieverTypeChange()
        }

        binding.TransferInputExchangeET.isFocusable = false
        binding.TransferInputExchangeET.setOnClickListener {
            viewModel.onExchangeClicked()
        }

        binding.TransferNextTV.setOnClickListener {
            viewModel.onClickNextButton()
        }

        viewModel.nextEvent.observe(viewLifecycleOwner) { nextEvent ->
            when (nextEvent) {
                TransferEvent.Submit -> {
                    findNavController().navigate(R.id.transfer_amount_fragment)
                }
                else -> Unit
            }
        }

        viewModel.recieverTypeEvent.observe(viewLifecycleOwner) { event ->
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
    }
}