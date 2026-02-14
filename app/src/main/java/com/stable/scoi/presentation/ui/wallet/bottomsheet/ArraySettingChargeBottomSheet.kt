package com.stable.scoi.presentation.ui.wallet.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentArraySettingChargeBottomsheetBinding
import com.stable.scoi.databinding.FragmentArraySettingTransferBottomsheetBinding

class ArraySettingChargeBottomSheet: BottomSheetDialogFragment() {
    private var _binding: FragmentArraySettingChargeBottomsheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var setArraySettingCharge: SetArraySettingCharge

    private var sortType: Sort? = null
    private var sortTypeString: String = ""
    private var categoryType: ChargeCategory? = null
    private var categoryTypeString: String = ""
    private var statusType: Status? = null
    private var statusTypeString: String = ""
    private var periodType: Period? = null
    private var periodTypeString: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArraySettingChargeBottomsheetBinding.inflate(inflater,container,false)
        return binding.root
    }


    // [핵심] 외부(Fragment/Activity)에서 리스너를 연결해주는 함수
    fun setCallback(listener: SetArraySettingCharge) {
        this.setArraySettingCharge = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            bottomsheetArraySettingArrayRecentTV.setOnClickListener {
                sortType = Sort.DESC
                arraySettingUpdate()
            }
            bottomsheetArraySettingArrayPastTV.setOnClickListener {
                sortType = Sort.ASC
                arraySettingUpdate()
            }

            bottomsheetArraySettingArrayTypeAllTV.setOnClickListener {
                categoryType = ChargeCategory.ALL
                arraySettingUpdate()
            }
            bottomsheetArraySettingArrayTypeWithdrawTV.setOnClickListener {
                categoryType = ChargeCategory.EXCHANGE
                arraySettingUpdate()
            }
            bottomsheetArraySettingArrayTypeDepositTV.setOnClickListener {
                categoryType = ChargeCategory.TOPUP
                arraySettingUpdate()
            }

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

            bottomsheetArraySettingStateStandbyTV.setOnClickListener {
                statusType = Status.PENDING
                arraySettingUpdate()
            }
            bottomsheetArraySettingStateCompleteTV.setOnClickListener {
                statusType = Status.COMPLETED
                arraySettingUpdate()
            }
            bottomsheetArraySettingStateCancelTV.setOnClickListener {
                statusType = Status.CANCELED
                arraySettingUpdate()
            }

            bottomsheetArraySettingSubmitBT.setOnClickListener {
                arraySetToString(sortType,categoryType,statusType,periodType)
                // [안전 장치] 리스너가 연결되어 있는지 확인
                if (::setArraySettingCharge.isInitialized) {
                    setArraySettingCharge.arraySettingCharge(
                        sortTypeString,
                        categoryTypeString,
                        statusTypeString,
                        periodTypeString
                    )
                }
                dismiss() // 창 닫기
            }
        }
    }

    private fun arraySettingUpdate() {
        when (categoryType) {
            ChargeCategory.EXCHANGE -> {
                setTextStyle(binding.bottomsheetArraySettingArrayTypeWithdrawTV, true)
                setTextStyle(binding.bottomsheetArraySettingArrayTypeAllTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayTypeDepositTV, false)
            }
            ChargeCategory.ALL -> {
                setTextStyle(binding.bottomsheetArraySettingArrayTypeWithdrawTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayTypeAllTV, true)
                setTextStyle(binding.bottomsheetArraySettingArrayTypeDepositTV, false)
            }
            ChargeCategory.TOPUP -> {
                setTextStyle(binding.bottomsheetArraySettingArrayTypeWithdrawTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayTypeAllTV, false)
                setTextStyle(binding.bottomsheetArraySettingArrayTypeDepositTV, true)
            }
            else -> Unit
        }

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

        when (statusType) {
            Status.PENDING -> {
                setTextStyle(binding.bottomsheetArraySettingStateStandbyTV, true)
                setTextStyle(binding.bottomsheetArraySettingStateCancelTV, false)
                setTextStyle(binding.bottomsheetArraySettingStateCompleteTV, false)
            }
            Status.CANCELED -> {
                setTextStyle(binding.bottomsheetArraySettingStateStandbyTV, false)
                setTextStyle(binding.bottomsheetArraySettingStateCancelTV, true)
                setTextStyle(binding.bottomsheetArraySettingStateCompleteTV, false)
            }
            Status.COMPLETED -> {
                setTextStyle(binding.bottomsheetArraySettingStateStandbyTV, false)
                setTextStyle(binding.bottomsheetArraySettingStateCancelTV, false)
                setTextStyle(binding.bottomsheetArraySettingStateCompleteTV, true)
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
        categoryType: ChargeCategory?,
        statusType: Status?,
        periodType: Period?) {
        sortTypeString = when (sortType) {
            Sort.ASC -> "asc"
            Sort.DESC -> "desc"
            else -> ""
        }
        categoryTypeString = when (categoryType) {
            ChargeCategory.TOPUP -> "CHARGE"
            ChargeCategory.ALL -> "ALL"
            ChargeCategory.EXCHANGE -> "CASH_EXCHANGE"
            else -> ""
        }
        statusTypeString = when (statusType) {
            Status.PENDING -> "WAIT"
            Status.COMPLETED -> "DONE"
            Status.CANCELED -> "CANCEL"
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