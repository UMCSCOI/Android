package com.stable.scoi.presentation.ui.home.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.stable.scoi.databinding.DialogSelectAccountBinding
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.setFragmentResult
import com.stable.scoi.R
import com.stable.scoi.extension.gone
import com.stable.scoi.extension.visible

class SelectAccountDialogFragment: DialogFragment() {

    private var selectedCoin: String? = null
    private val binding: DialogSelectAccountBinding by lazy {
        DialogSelectAccountBinding.inflate(layoutInflater)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    private fun initView() {
        binding.apply {
            imageClose.setOnClickListener {
                dismiss()
            }

            buttonNext.setOnClickListener {
                if (selectedCoin.isNullOrEmpty()) return@setOnClickListener
                val result = Bundle().apply {
                    putString("bundleKey_coin", selectedCoin)
                }
                setFragmentResult("requestKey_coin", result)
                dismiss()
            }

            textUsdt.setOnClickListener {
                imageUsdtCheck.visible()
                imageUsdcCheck.gone()
                selectedCoin = "USDT"
                buttonNext.setBackgroundResource(R.drawable.bg_rect_active_fill_radius60)
                buttonText.setTextColor(ContextCompat.getColor(requireActivity(), R.color.active))
            }

            textUsdc.setOnClickListener {
                imageUsdcCheck.visible()
                imageUsdtCheck.gone()
                selectedCoin = "USDC"
                buttonNext.setBackgroundResource(R.drawable.bg_rect_active_fill_radius60)
                buttonText.setTextColor(ContextCompat.getColor(requireActivity(), R.color.active))
            }
        }
    }
}