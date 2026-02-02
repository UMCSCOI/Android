package com.stable.scoi.presentation.ui.transfer

import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentTransferBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.transfer.dialog.BookMarkDialogFragment
import com.stable.scoi.presentation.ui.transfer.recyclerview.BookMarkOnClickListener
import com.stable.scoi.presentation.ui.transfer.recyclerview.BookMarkRVAdapter
import com.stable.scoi.presentation.ui.transfer.bottomsheet.ExchangeBottomSheet
import com.stable.scoi.domain.model.transfer.ReceiverType
import com.stable.scoi.presentation.ui.transfer.bottomsheet.ReceiverTypeBottomSheet
import com.stable.scoi.presentation.ui.transfer.recyclerview.RecentOnCliCKListener
import com.stable.scoi.presentation.ui.transfer.recyclerview.RecentRVAdapter
import com.stable.scoi.presentation.ui.transfer.bottomsheet.SetExchangeType
import com.stable.scoi.presentation.ui.transfer.bottomsheet.SetReceiverType
import com.stable.scoi.presentation.ui.transfer.dialog.SetBookmark
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransferFragment : RecentOnCliCKListener, BookMarkOnClickListener, SetBookmark,
    SetReceiverType, SetExchangeType,
    BaseFragment<FragmentTransferBinding, TransferState, TransferEvent, TransferViewModel>(
    FragmentTransferBinding::inflate
) {
    private var bookmarkData = ArrayList<BookMark>()
    private var recentData = ArrayList<Recent>()

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

        binding.TransferBackArrowIV.setOnClickListener {
            //main으로 이동
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

        bookmarkData.apply {
            add(BookMark("1", "홍길동", "주소", "UPBIT", true))
            add(BookMark("2", "홍길동", "주소", "UPBIT", true))
            add(BookMark("3", "홍길동", "주소", "UPBIT", true))
            add(BookMark("4", "홍길동", "주소", "UPBIT", true))
            add(BookMark("5", "홍길동", "주소", "UPBIT", true))
        }
            //더미 데이터1


        recentData.apply {
            add(Recent("1", "홍길동", "주소", "UPBIT", true))
            add(Recent("2", "홍길동", "주소", "UPBIT", true))
            add(Recent("3", "홍길동", "주소", "UPBIT", true))
            add(Recent("4", "홍길동", "주소", "UPBIT", true))
            add(Recent("5", "홍길동", "주소", "UPBIT", true))
        }
            //더미 데이터2

        val bookMarkRVAdapter = BookMarkRVAdapter(bookmarkData, this)
        val recentRVAdapter = RecentRVAdapter(recentData, this)
        binding.TransferBookmarkRV.adapter = bookMarkRVAdapter
        binding.TransferBookmarkRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        binding.TransferBookmarkTV.setOnClickListener {
            binding.apply {
                TransferRecentTV.typeface = ResourcesCompat.getFont(requireContext(), R.font.pretendard_regular)
                TransferBookmarkTV.typeface = ResourcesCompat.getFont(requireContext(),R.font.pretendard_semibold)
                TransferRecentTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                TransferBookmarkTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                //TextAppearance로 통합 예정
                TransferBookmarkRV.adapter = bookMarkRVAdapter
                TransferBookmarkRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }

        binding.TransferRecentTV.setOnClickListener {
            binding.apply {
                TransferRecentTV.typeface = ResourcesCompat.getFont(requireContext(), R.font.pretendard_semibold)
                TransferBookmarkTV.typeface = ResourcesCompat.getFont(requireContext(),R.font.pretendard_regular)
                TransferRecentTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                TransferBookmarkTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                //TextAppearance로 통합 예정
                TransferBookmarkRV.adapter = recentRVAdapter
                TransferBookmarkRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }

        binding.TransferBookmarkAddTV.setOnClickListener {
            BookMarkDialogFragment().show(
                childFragmentManager,
                "Dialogfragment"
            )
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


    //ReceiverTypeBottomSheet
    override fun individual() {
        viewModel.setReceiverTypeIndividual()
    }

    override fun corporation() {
        viewModel.setReceiverTypeCorporation()
    }


    //ExchangeTypeBottomSheet
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


    //BookMarkDialog
    override fun inputString(name: String, address: String) {
        viewModel.submitBookMarkReceiver(name,address)
    }

    override fun setExchangeUPBIT() {
        viewModel.setBookMarkExchangeUpbit()
    }

    override fun setExchangeBITHUMB() {
        viewModel.setBookMarkExchangeBithumb()
    }

    override fun setExchangeBINANCE() {
        viewModel.setBookMarkExchangeBinance()
    }

    override fun removeFocus(editText: EditText) {
        viewModel.focusRemove(editText)
    }


    //RVAdatper
    override fun rcOnclickListener(recent: Recent) {
        viewModel.submitReceiver(recent.recipientName, recent.walletAddress)
        changeStringToExchangeType(recent.exchangeType)
        findNavController().navigate(R.id.transfer_amount_fragment)
    }

    override fun bmOnclickListener(bookMark: BookMark) {
        viewModel.submitReceiver(bookMark.recipientName, bookMark.walletAddress)
        changeStringToExchangeType(bookMark.exchangeType)
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
            "BINANCE" -> {
                viewModel.setExchangeBinance()
            }
        }
    }

}