package com.stable.scoi.presentation.ui.home.dialog

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.stable.scoi.R
import com.stable.scoi.databinding.DialogSelectStableBinding

class SelectStableDialogFragment: DialogFragment() {

    private var selectedCoin: String = "USDT"
    private val binding: DialogSelectStableBinding by lazy {
        DialogSelectStableBinding.inflate(layoutInflater)
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
            val customTypeface = ResourcesCompat.getFont(requireActivity(), R.font.pretendard_semibold)
            textTitle.text = buildSpannedString {
                append("어떤 ")

                customTypeface?.let { tf ->
                    inSpans(CustomTypefaceSpan(tf)) {
                        append("스테이블 코인")
                    }
                } ?: bold { append("스테이블 코인") } // 폰트 로드 실패 시 기본 bold 처리

                append("으로\n결제할까요?")
            }

            imageClose.setOnClickListener {
                dismiss()
            }

            buttonNext.setOnClickListener {
                if (selectedCoin.isEmpty()) return@setOnClickListener
                val result = Bundle().apply {
                    putString("bundleKey_coin", selectedCoin)
                }
                setFragmentResult("requestKey_coin", result)
                dismiss()
            }

            layoutUsdt.setOnClickListener {
                selectedCoin = "USDT"

                layoutUsdt.setBackgroundResource(R.drawable.bg_rect_sky_blue_stroke_1_5_active_radius_20)
                layoutUsdc.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
                textUsdt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.active))
                textUsdc.setTextColor(ContextCompat.getColor(requireActivity(), R.color.sub_gray_1))

                buttonNext.setBackgroundResource(R.drawable.bg_rect_active_fill_radius60)
                buttonText.setTextColor(ContextCompat.getColor(requireActivity(), R.color.active))
            }

            layoutUsdc.setOnClickListener {
                selectedCoin = "USDC"

                layoutUsdc.setBackgroundResource(R.drawable.bg_rect_sky_blue_stroke_1_5_active_radius_20)
                layoutUsdt.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
                textUsdc.setTextColor(ContextCompat.getColor(requireActivity(), R.color.active))
                textUsdt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.sub_gray_1))

                buttonNext.setBackgroundResource(R.drawable.bg_rect_active_fill_radius60)
                buttonText.setTextColor(ContextCompat.getColor(requireActivity(), R.color.active))
            }
        }
    }
}

class CustomTypefaceSpan(private val typeface: Typeface) : MetricAffectingSpan() {
    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, typeface)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, typeface)
    }

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
        paint.typeface = tf
    }
}