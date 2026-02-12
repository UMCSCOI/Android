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

    private lateinit var binding: FragmentArraySettingChargeBottomsheetBinding

    private lateinit var setArraySettingCharge: SetArraySettingCharge

    var sortType: Sort? = null
    lateinit var sortTypeString: String
    var categoryType: ChargeCategory? = null
    lateinit var categoryTypeString: String
    var statusType: Status? = null
    lateinit var statusTypeString: String
    var periodType: Period? = null
    lateinit var periodTypeString: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArraySettingChargeBottomsheetBinding.inflate(inflater,container,false)
        return binding.root
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
                setArraySettingCharge.arraySettingCharge(sortTypeString,categoryTypeString,statusTypeString,periodTypeString)
                dismiss()
            }
        }
    }

    private fun arraySettingUpdate() {
        when (categoryType) {
            ChargeCategory.EXCHANGE -> {
                binding.apply {
                    bottomsheetArraySettingArrayTypeWithdrawTV.setTextAppearance(R.style.l18_sb)
                    bottomsheetArraySettingArrayTypeWithdrawTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                    bottomsheetArraySettingArrayTypeAllTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayTypeAllTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayTypeDepositTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayTypeDepositTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                }
            }
            ChargeCategory.ALL -> {
                binding.apply {
                    bottomsheetArraySettingArrayTypeWithdrawTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayTypeWithdrawTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayTypeAllTV.setTextAppearance(R.style.l18_sb)
                    bottomsheetArraySettingArrayTypeAllTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                    bottomsheetArraySettingArrayTypeDepositTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayTypeDepositTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                }
            }
            ChargeCategory.TOPUP -> {
                binding.apply {
                    bottomsheetArraySettingArrayTypeWithdrawTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayTypeWithdrawTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayTypeAllTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayTypeAllTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayTypeDepositTV.setTextAppearance(R.style.l18_sb)
                    bottomsheetArraySettingArrayTypeDepositTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                }
            }
            else -> Unit
        }

        when (periodType) {
            Period.TODAY -> {
                binding.apply {
                    bottomsheetArraySettingArrayPeriodTodayTV.setTextAppearance(R.style.l18_sb)
                    bottomsheetArraySettingArrayPeriodTodayTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                    bottomsheetArraySettingArrayPeriod1monthTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayPeriod1monthTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayPeriod3monthTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayPeriod3monthTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayPeriod6monthTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayPeriod6monthTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                }
            }
            Period.ONEMONTH -> {
                binding.apply {
                    bottomsheetArraySettingArrayPeriodTodayTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayPeriodTodayTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayPeriod1monthTV.setTextAppearance(R.style.l18_sb)
                    bottomsheetArraySettingArrayPeriod1monthTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                    bottomsheetArraySettingArrayPeriod3monthTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayPeriod3monthTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayPeriod6monthTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayPeriod6monthTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                }
            }
            Period.SIXMONTH -> {
                binding.apply {
                    bottomsheetArraySettingArrayPeriodTodayTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayPeriodTodayTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayPeriod1monthTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayPeriod1monthTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayPeriod3monthTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayPeriod3monthTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayPeriod6monthTV.setTextAppearance(R.style.l18_sb)
                    bottomsheetArraySettingArrayPeriod6monthTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                }
            }
            Period.THREEMONTH -> {
                binding.apply {
                    bottomsheetArraySettingArrayPeriodTodayTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayPeriodTodayTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayPeriod1monthTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayPeriod1monthTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayPeriod3monthTV.setTextAppearance(R.style.l18_sb)
                    bottomsheetArraySettingArrayPeriod3monthTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                    bottomsheetArraySettingArrayPeriod6monthTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayPeriod6monthTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                }
            }
            else -> Unit
        }

        when (sortType) {
            Sort.ASC -> {
                binding.apply {
                    bottomsheetArraySettingArrayRecentTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayRecentTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayPastTV.setTextAppearance(R.style.l18_sb)
                    bottomsheetArraySettingArrayPastTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                }
            }
            Sort.DESC -> {
                binding.apply {
                    bottomsheetArraySettingArrayRecentTV.setTextAppearance(R.style.l18_sb)
                    bottomsheetArraySettingArrayRecentTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                    bottomsheetArraySettingArrayPastTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayPastTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                }
            }
            else -> Unit
        }

        when (statusType) {
            Status.PENDING -> {
                binding.apply {
                    bottomsheetArraySettingStateStandbyTV.setTextAppearance(R.style.l18_sb)
                    bottomsheetArraySettingStateStandbyTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                    bottomsheetArraySettingStateCancelTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingStateCancelTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingStateCompleteTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingStateCompleteTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                }
            }
            Status.CANCELED -> {
                binding.apply {
                    bottomsheetArraySettingStateStandbyTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingStateStandbyTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingStateCancelTV.setTextAppearance(R.style.l18_sb)
                    bottomsheetArraySettingStateCancelTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                    bottomsheetArraySettingStateCompleteTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingStateCompleteTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                }
            }
            Status.COMPLETED -> {
                binding.apply {
                    bottomsheetArraySettingStateStandbyTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingStateStandbyTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingStateCancelTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingStateCancelTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingStateCompleteTV.setTextAppearance(R.style.l18_sb)
                    bottomsheetArraySettingStateCompleteTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                }
            }
            else -> Unit
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
}