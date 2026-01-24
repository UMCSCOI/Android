package com.stable.scoi.presentation.base

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
import okhttp3.internal.cache.DiskLruCache

@AndroidEntryPoint
class TransferFragment : SetReceiverType, SetExchangeType ,BaseFragment<FragmentTransferBinding, TransferState, TransferEvent, TransferViewModel>(
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
            ExchangeBottomSheet().show(
                childFragmentManager,
                "BottomSheet"
            )
        }

        binding.TransferInputNameET.text.toString()

        binding.TransferNextTV.setOnClickListener {
            val name: String = binding.TransferInputNameET.text.toString()
            val address: String = binding.TransferInputAddressET.text.toString()
            viewModel.submitReceiver(name,address)
            viewModel.onClickNextButton()
        }

        viewModel.focusRemove(binding.TransferInputNameET)
        viewModel.focusRemove(binding.TransferInputAddressET)
        viewModel.focusRemove(binding.TransferCorpNameENGET)
        viewModel.focusRemove(binding.TransferCorpNameKORET)



        //output
        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.receiverType.collect { receiverType ->
                    when (receiverType) {
                        ReceiverType.Empty -> {
                            binding.TransferInputNameET.isFocusable = false
                        }

                        ReceiverType.Individual -> {
                            individualTypeView()
                        }

                        ReceiverType.Corporation -> {
                            corporationTypeView()
                        }
                    }
                }
            }

            launch {
                viewModel.receiver.collect { receiver ->
                    if (receiver.receiverName == "") {
                        binding.TransferInputNameWarningTV.visibility = View.VISIBLE
                    } else {
                        binding.TransferInputNameWarningTV.visibility = View.GONE
                    }

                    if (receiver.receiverAddress == "") {
                        binding.TransferInputAddressWarningTV.visibility = View.VISIBLE
                    } else {
                        binding.TransferInputAddressWarningTV.visibility = View.GONE
                    }
                }
            }

            launch {
                viewModel.exchangeType.collect { exchange ->
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

            launch {
                viewModel.uiEvent.collect { event ->
                    when (event) {
                        TransferEvent.OpenReceiverType -> {
                            ReceiverTypeBottomSheet().show(
                                childFragmentManager,
                                "BottomSheet"
                            )
                        }
                        TransferEvent.NavigateToNextPage -> {
                            findNavController().navigate(R.id.transfer_amount_fragment)
                        }
                    }
                }
            }
        }
    }

    fun individualTypeView() {
        binding.apply {
            TransferInputNameET.isFocusable = true
            TransferInputNameET.isFocusableInTouchMode = true
            TransferInputNameET.requestFocus()
            TransferReceiverTypeTV.visibility = View.VISIBLE
            TransferReceiverTypeTV.text = "개인"
            TransferCorpNameENGET.visibility = View.GONE
            TransferCorpNameENGTV.visibility = View.GONE
            TransferCorpNameKORET.visibility = View.GONE
            TransferCorpNameKORTV.visibility = View.GONE
        }
    }
    fun corporationTypeView() {
        binding.apply {
            TransferInputNameET.isFocusable = true
            TransferInputNameET.isFocusableInTouchMode = true
            TransferInputNameET.requestFocus()
            TransferReceiverTypeTV.visibility = View.VISIBLE
            TransferReceiverTypeTV.text = "법인"
            TransferCorpNameENGET.visibility = View.VISIBLE
            TransferCorpNameENGTV.visibility = View.VISIBLE
            TransferCorpNameKORET.visibility = View.VISIBLE
            TransferCorpNameKORTV.visibility = View.VISIBLE
        }
    }

    override fun individual() {
        viewModel.setReceiverTypeIndividual()
    }

    override fun corporation() {
        viewModel.setReceiverTypeCorporation()
    }

    override fun upbit() {
        viewModel.setExchangeUpbit()
    }

    override fun bithumb() {
        viewModel.setExchangeBithumb()
    }

    override fun binance() {
        viewModel.setExchangeBinance()
    }

    override fun empty() {
        viewModel.setExchange()
    }
}