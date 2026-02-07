package com.stable.scoi.presentation.ui.transfer.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentBookMarkDialogBinding
import com.stable.scoi.presentation.ui.transfer.dialog.Select

class BookMarkDialogFragment: DialogFragment(){
    lateinit var binding: FragmentBookMarkDialogBinding

    private lateinit var setBookmark: SetBookmark

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setBookmark = parentFragment as SetBookmark
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookMarkDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var selectedType: Select? = null

        setBookmark.removeFocus(binding.bookmarkInputNameET)
        setBookmark.removeFocus(binding.bookmarkInputAddressET)

        binding.bookmarkCloseIv.setOnClickListener {
            dismiss()
        }

        binding.apply {
            bookmarkExchangeUpbitBT.setOnClickListener {
                setBookmark.setExchangeUPBIT()
                selectedType = Select.UPBIT
                updateExchange(selectedType)
                checkEmptyExchange(selectedType)
            }
            bookmarkExchangeBithumbBT.setOnClickListener {
                setBookmark.setExchangeBITHUMB()
                selectedType = Select.BITHUMB
                updateExchange(selectedType)
                checkEmptyExchange(selectedType)
            }


            bookmarkInputNameRemoveIV.setOnClickListener {
                bookmarkInputNameET.setText("")
            }
            bookmarkInputAddressRemoveIV.setOnClickListener {
                bookmarkInputAddressET.setText("")
            }
        }

        binding.bookmarkSaveBT.setOnClickListener {
            val name = binding.bookmarkInputNameET.text.toString()
            val address = binding.bookmarkInputAddressET.text.toString()
            checkEmptyString(name, address)
            checkEmptyExchange(selectedType)

            if (name == "" || address == "" || selectedType == null) {
                Unit
            }
            else {
                setBookmark.inputString(name,address)
                dismiss()
            }
        }
    }

    private fun updateExchange(selectedType: Select?) {
        binding.apply {
            when (selectedType) {
                Select.UPBIT -> {
                    bookmarkExchangeUpbitBT.setBackgroundResource(R.drawable.select_exchange_pressed)
                    bookmarkExchangeBithumbBT.setBackgroundResource(R.drawable.select_exchange)
                    bookmarkExchangeBinanceBT.setBackgroundResource(R.drawable.select_exchange)
                }
                Select.BITHUMB -> {
                    bookmarkExchangeUpbitBT.setBackgroundResource(R.drawable.select_exchange)
                    bookmarkExchangeBithumbBT.setBackgroundResource(R.drawable.select_exchange_pressed)
                    bookmarkExchangeBinanceBT.setBackgroundResource(R.drawable.select_exchange)
                }
                Select.BINANCE -> {
                    bookmarkExchangeUpbitBT.setBackgroundResource(R.drawable.select_exchange)
                    bookmarkExchangeBithumbBT.setBackgroundResource(R.drawable.select_exchange)
                    bookmarkExchangeBinanceBT.setBackgroundResource(R.drawable.select_exchange_pressed)
                }
                else -> Unit
            }
        }
    }

    private fun checkEmptyString(name: String, address: String) {
        if (name == "") {
            binding.bookmarkInputNameWarningTV.visibility = View.VISIBLE
        }
        else {
            binding.bookmarkInputNameWarningTV.visibility = View.GONE
        }

        if (address == "") {
            binding.bookmarkInputAddressWarningTV.visibility = View.VISIBLE
        }
        else {
            binding.bookmarkInputAddressWarningTV.visibility = View.GONE
        }
    }

    private fun checkEmptyExchange(selectedType: Select?) {
        if (selectedType == null) {
            binding.bookmarkInputExchangeWarningTV.visibility = View.VISIBLE
        }
        else {
            binding.bookmarkInputExchangeWarningTV.visibility = View.GONE
        }
    }

}