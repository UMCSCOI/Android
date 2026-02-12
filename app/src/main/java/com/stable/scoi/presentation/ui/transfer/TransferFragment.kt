package com.stable.scoi.presentation.ui.transfer

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentTransferBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.transfer.bottomsheet.ExchangeBottomSheet
import com.stable.scoi.presentation.ui.transfer.bottomsheet.SetExchangeType
import com.stable.scoi.presentation.ui.transfer.recyclerview.DirectoryOnClickListener
import com.stable.scoi.presentation.ui.transfer.recyclerview.DirectoryRVAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransferFragment : DirectoryOnClickListener, SetExchangeType,
    BaseFragment<FragmentTransferBinding, TransferState, TransferEvent, TransferViewModel>(
    FragmentTransferBinding::inflate
) {
    private var directoryData = ArrayList<Directory>()

    override val viewModel: TransferViewModel by activityViewModels()

    override fun initView() {

        binding.TransferNextTV.isEnabled = false

        val watcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val value1 = binding.TransferInputNameET.text.toString()
                val value2 = binding.TransferInputName1ENGET.text.toString()
                val value3 = binding.TransferInputName2ENGET.text.toString()
                val value4 = binding.TransferInputAddressET.text.toString()

                updateButtonState(value1, value2, value3, value4, viewModel.exchangeType.value)
            }
        }

        binding.TransferDirectoryIcIV.setOnClickListener {
            binding.TransferDirectoryIcPopupTV.visibility = View.VISIBLE
            binding.TransferDirectoryIcPopupIV.visibility = View.VISIBLE
        }

        binding.root.setOnClickListener {
            binding.TransferDirectoryIcPopupTV.visibility = View.GONE
            binding.TransferDirectoryIcPopupIV.visibility = View.GONE
        }

        binding.TransferInputNameET.addTextChangedListener(watcher)
        binding.TransferInputName1ENGET.addTextChangedListener(watcher)
        binding.TransferInputName2ENGET.addTextChangedListener(watcher)
        binding.TransferInputAddressET.addTextChangedListener(watcher)

        binding.TransferInputExchangeET.isFocusable = false
        binding.TransferInputExchangeET.setOnClickListener {
            ExchangeBottomSheet().show(
                childFragmentManager,
                "BottomSheet"
            )
        }

        binding.TransferNextTV.setOnClickListener {
            val nameKOR: String = binding.TransferInputNameET.text.toString()
            val nameENG: String =
                binding.TransferInputName1ENGET.text.toString() + " " + binding.TransferInputName2ENGET.text.toString()
            val address: String = binding.TransferInputAddressET.text.toString()
            viewModel.submitReceiver(nameKOR, nameENG, address)
            viewModel.onClickNextButton()
        }

        binding.TransferBackArrowIV.setOnClickListener {
            //main으로 이동
        }

        viewModel.focusRemove(binding.TransferInputNameET)
        viewModel.focusRemove(binding.TransferInputAddressET)
        viewModel.focusRemove(binding.TransferCorpNameENGET)
        viewModel.focusRemove(binding.TransferCorpNameKORET)


        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.exchangeType.collect { exchange ->
                    val value1 = binding.TransferInputNameET.text.toString()
                    val value2 = binding.TransferInputName1ENGET.text.toString()
                    val value3 = binding.TransferInputName2ENGET.text.toString()
                    val value4 = binding.TransferInputAddressET.text.toString()

                    updateButtonState(value1, value2, value3, value4, exchange)

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
                        TransferEvent.NavigateToNextPage -> {
                            findNavController().navigate(R.id.transfer_amount_fragment)
                        }
                    }
                }
            }
        }

        directoryData.apply {
            add(Directory("1", "홍길동", "Hong GIldong", "주소", "UPBIT", true))
            add(Directory("2", "홍길동", "Hong GIldong", "주소", "UPBIT", true))
            add(Directory("3", "홍길동", "Hong GIldong", "주소", "UPBIT", true))
            add(Directory("4", "홍길동", "Hong GIldong", "주소", "UPBIT", true))
            add(Directory("5", "홍길동", "Hong GIldong", "주소", "UPBIT", true))
        }

        val directoryRVAdapter = DirectoryRVAdapter(directoryData, this)
        binding.TransferBookmarkRV.adapter = directoryRVAdapter
        binding.TransferBookmarkRV.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }


    //ExchangeTypeBottomSheet
    override fun upbit() {
        viewModel.setExchangeUpbit()
    }

    override fun bithumb() {
        viewModel.setExchangeBithumb()
    }

    override fun empty() {
        viewModel.setExchange()
    }


    //RVAdatper
    override fun dtOnclickListener(directory: Directory) {
        viewModel.submitReceiver(directory.recipientKORName, directory.recipientENGName, directory.walletAddress)
        changeStringToExchangeType(directory.exchangeType)
        findNavController().navigate(R.id.transfer_amount_fragment)
    }

    private fun changeStringToExchangeType(exchange: String) {
        when (exchange) {
            "UPBIT" -> {
                viewModel.setExchangeUpbit()
            }
            "BITHUMB" -> {
                viewModel.setExchangeBithumb()
            }
        }
    }

    private fun updateButtonState(value1: String, value2: String, value3: String, value4: String, exchange: Exchange) {
        if(value1 == "" || value2 == "" || value3 == "" || value4 == "" || exchange == Exchange.Empty || exchange == Exchange.Unselected
        ) {
            Unit
        }
        else binding.TransferNextTV.isEnabled = true
    }

}