package com.stable.scoi.presentation.ui.wallet.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentArraySettingTransferBottomsheetBinding

class ArraySettingTransferBottomSheet: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentArraySettingTransferBottomsheetBinding

    var sortType: Sort? = null
    var categoryType: TransferCategory? = null
    var periodType: Period? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArraySettingTransferBottomsheetBinding.inflate(inflater,container,false)
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

            bottomsheetArraySettingSubmitBT.setOnClickListener {
                //submit()
                dismiss()
            }
        }
    }

    private fun arraySettingUpdate() {
        when (categoryType) {
            TransferCategory.IN -> {
                binding.apply {
                    bottomsheetArraySettingArrayTypeReceiveTV.setTextAppearance(R.style.l18_sb)
                    bottomsheetArraySettingArrayTypeReceiveTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                    bottomsheetArraySettingArrayTypeAllTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayTypeAllTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayTypeSendTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayTypeSendTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                }
            }
            TransferCategory.ALL -> {
                binding.apply {
                    bottomsheetArraySettingArrayTypeReceiveTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayTypeReceiveTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayTypeAllTV.setTextAppearance(R.style.l18_sb)
                    bottomsheetArraySettingArrayTypeAllTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                    bottomsheetArraySettingArrayTypeSendTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayTypeSendTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                }
            }
            TransferCategory.OUT -> {
                binding.apply {
                    bottomsheetArraySettingArrayTypeReceiveTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayTypeReceiveTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayTypeAllTV.setTextAppearance(R.style.l18_rg)
                    bottomsheetArraySettingArrayTypeAllTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
                    bottomsheetArraySettingArrayTypeSendTV.setTextAppearance(R.style.l18_sb)
                    bottomsheetArraySettingArrayTypeSendTV.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
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
    }
}