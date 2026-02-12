package com.stable.scoi.presentation.ui.wallet.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentArraySettingTransferBottomsheetBinding

class ArraySettingTransferBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentArraySettingTransferBottomsheetBinding? = null
    private val binding get() = _binding!!

    // 리스너 변수
    private lateinit var setArraySettingTransfer: SetArraySettingTransfer

    // 선택 상태 변수
    private var sortType: Sort? = null
    private var sortTypeString: String = ""

    private var categoryType: TransferCategory? = null
    private var categoryTypeString: String = ""

    private var periodType: Period? = null
    private var periodTypeString: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArraySettingTransferBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    // [핵심] 외부(Fragment/Activity)에서 리스너를 연결해주는 함수
    fun setCallback(listener: SetArraySettingTransfer) {
        this.setArraySettingTransfer = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            // 정렬 (최신순/과거순)
            bottomsheetArraySettingArrayRecentTV.setOnClickListener {
                sortType = Sort.DESC
                arraySettingUpdate()
            }
            bottomsheetArraySettingArrayPastTV.setOnClickListener {
                sortType = Sort.ASC
                arraySettingUpdate()
            }

            // 유형 (전체/입금/출금)
            bottomsheetArraySettingArrayTypeAllTV.setOnClickListener {
                categoryType = TransferCategory.ALL
                arraySettingUpdate()
            }
            bottomsheetArraySettingArrayTypeSendTV.setOnClickListener {
                categoryType = TransferCategory.OUT
                arraySettingUpdate()
            }
            bottomsheetArraySettingArrayTypeReceiveTV.setOnClickListener {
                categoryType = TransferCategory.IN
                arraySettingUpdate()
            }

            // 기간 (오늘/1개월/3개월/6개월)
            bottomsheetArraySettingArrayPeriodTodayTV.setOnClickListener {
                periodType = Period.TODAY
                arraySettingUpdate()
            }
            bottomsheetArraySettingArrayPeriod1monthTV.setOnClickListener {
                periodType = Period.ONEMONTH
                arraySettingUpdate()
            }
            bottomsheetArraySettingArrayPeriod3monthTV.setOnClickListener {
                periodType = Period.THREEMONTH
                arraySettingUpdate()
            }
            bottomsheetArraySettingArrayPeriod6monthTV.setOnClickListener {
                periodType = Period.SIXMONTH
                arraySettingUpdate()
            }

            // 완료 버튼
            bottomsheetArraySettingSubmitBT.setOnClickListener {
                arraySetToString(sortType, categoryType, periodType)

                // [안전 장치] 리스너가 연결되어 있는지 확인
                if (::setArraySettingTransfer.isInitialized) {
                    setArraySettingTransfer.arraySettingTransfer(
                        sortTypeString,
                        categoryTypeString,
                        periodTypeString
                    )
                }
                dismiss() // 창 닫기
            }
        }
    }

    // UI 업데이트 로직 (선택된 항목 색상 변경)
    private fun arraySettingUpdate() {
        // Category UI Update
        when (categoryType) {
            TransferCategory.IN -> {
                setTextStyle(binding.bottomsheetArraySettingArrayTypeReceiveTV, true)
                setTextStyle(binding.bottomsheetArraySettingArrayTypeAllTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayTypeSendTV, false)
            }
            TransferCategory.ALL -> {
                setTextStyle(binding.bottomsheetArraySettingArrayTypeReceiveTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayTypeAllTV, true)
                setTextStyle(binding.bottomsheetArraySettingArrayTypeSendTV, false)
            }
            TransferCategory.OUT -> {
                setTextStyle(binding.bottomsheetArraySettingArrayTypeReceiveTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayTypeAllTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayTypeSendTV, true)
            }
            else -> Unit
        }

        // Period UI Update
        when (periodType) {
            Period.TODAY -> {
                setTextStyle(binding.bottomsheetArraySettingArrayPeriodTodayTV, true)
                setTextStyle(binding.bottomsheetArraySettingArrayPeriod1monthTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayPeriod3monthTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayPeriod6monthTV, false)
            }
            Period.ONEMONTH -> {
                setTextStyle(binding.bottomsheetArraySettingArrayPeriodTodayTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayPeriod1monthTV, true)
                setTextStyle(binding.bottomsheetArraySettingArrayPeriod3monthTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayPeriod6monthTV, false)
            }
            Period.SIXMONTH -> {
                setTextStyle(binding.bottomsheetArraySettingArrayPeriodTodayTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayPeriod1monthTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayPeriod3monthTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayPeriod6monthTV, true)
            }
            Period.THREEMONTH -> {
                setTextStyle(binding.bottomsheetArraySettingArrayPeriodTodayTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayPeriod1monthTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayPeriod3monthTV, true)
                setTextStyle(binding.bottomsheetArraySettingArrayPeriod6monthTV, false)
            }
            else -> Unit
        }

        // Sort UI Update
        when (sortType) {
            Sort.ASC -> {
                setTextStyle(binding.bottomsheetArraySettingArrayRecentTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayPastTV, true)
            }
            Sort.DESC -> {
                setTextStyle(binding.bottomsheetArraySettingArrayRecentTV, true)
                setTextStyle(binding.bottomsheetArraySettingArrayPastTV, false)
            }
            else -> Unit
        }
    }

    // 텍스트 스타일 변경 헬퍼 함수 (코드 중복 제거용)
    private fun setTextStyle(textView: android.widget.TextView, isSelected: Boolean) {
        if (isSelected) {
            textView.setTextAppearance(R.style.l18_sb)
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
        } else {
            textView.setTextAppearance(R.style.l18_rg)
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
        }
    }

    private fun arraySetToString(
        sortType: Sort?,
        categoryType: TransferCategory?,
        periodType: Period?
    ) {
        sortTypeString = when (sortType) {
            Sort.ASC -> "asc"
            Sort.DESC -> "desc"
            else -> ""
        }
        categoryTypeString = when (categoryType) {
            TransferCategory.ALL -> "ALL"
            TransferCategory.IN -> "DEPOSIT"
            TransferCategory.OUT -> "WITHDRAW"
            else -> ""
        }
        periodTypeString = when (periodType) {
            Period.TODAY -> "TODAY"
            Period.SIXMONTH -> "SIX_MONTHS"
            Period.ONEMONTH -> "ONE_MONTH"
            Period.THREEMONTH -> "THREE_MONTHS"
            else -> ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}