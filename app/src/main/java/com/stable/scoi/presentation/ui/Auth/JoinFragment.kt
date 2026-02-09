package com.stable.scoi.presentation.ui.Auth

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentJoinBinding
import com.stable.scoi.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinFragment : BaseFragment<FragmentJoinBinding, JoinState, JoinEvent, JoinViewModel>(
    FragmentJoinBinding::inflate
) {
    override val viewModel: JoinViewModel by activityViewModels()


    private var isRearNumberVisible = false

    override fun initView() {
        binding.joinBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.koreanNameEt.doOnTextChanged { text, _, _, _ ->
            val input = text.toString()
            val koreanPattern = Regex("^[가-힣ㄱ-ㅎㅏ-ㅣ]+$")

            val isKorean = koreanPattern.matches(input)
            val isLengthEnough = input.length > 1
            val isValid = isKorean && isLengthEnough

            if (input.isNotEmpty()) {
                binding.koreanNameClearIv.visibility = View.VISIBLE

                if (isValid) {
                    binding.koreanNameClearIv.setImageResource(R.drawable.check_default)

                    binding.koreanSelectedNameLine.visibility = View.VISIBLE
                    binding.koreanNameLine.visibility = View.GONE
                    binding.koreanErrorNameLine.visibility = View.GONE
                    binding.koreanErrorTv.visibility = View.GONE


                } else {
                    binding.koreanNameClearIv.setImageResource(R.drawable.no_m)
                    binding.koreanNameClearIv.isClickable = true


                    if (!isKorean) {
                        binding.koreanSelectedNameLine.visibility = View.GONE
                        binding.koreanErrorNameLine.visibility = View.VISIBLE
                        binding.koreanErrorTv.visibility = View.VISIBLE
                    } else {
                        binding.koreanSelectedNameLine.visibility = View.VISIBLE
                        binding.koreanErrorNameLine.visibility = View.GONE
                        binding.koreanErrorTv.visibility = View.GONE
                    }
                }
            } else {

                binding.koreanNameClearIv.visibility = View.GONE
                binding.koreanNameLine.visibility = View.VISIBLE
                binding.koreanSelectedNameLine.visibility = View.GONE
                binding.koreanErrorNameLine.visibility = View.GONE
            }
        }
        binding.engLastNameEt.doOnTextChanged { text, _, _, _ ->
            val input = text.toString()
            val engPattern = Regex("^[A-z]+$")

            val isEng = engPattern.matches(input)
            val isLengthEnough = input.length > 1
            val isValid = isEng && isLengthEnough


            if (input.isNotEmpty()) {
                binding.engLastNameClearIv.visibility = View.VISIBLE
                if (isValid) {
                    binding.engLastNameLine.visibility = View.GONE
                    binding.engErrorLastNameLine.visibility = View.GONE
                    binding.engLastNameErrorTv.visibility = View.GONE
                    binding.engSelectedFirstNameLine.visibility=View.GONE
                    binding.engLastNameClearIv.visibility=View.GONE

                } else {
                    binding.engLastNameClearIv.setImageResource(R.drawable.no_m)
                    binding.engLastNameClearIv.isClickable = true

                }
                if (!isEng) {
                    binding.engSelectedLastNameLine.visibility = View.GONE
                    binding.engErrorLastNameLine.visibility = View.VISIBLE
                    binding.engLastNameErrorTv.visibility = View.VISIBLE
                } else {
                    binding.engSelectedLastNameLine.visibility = View.VISIBLE
                    binding.engErrorLastNameLine.visibility = View.GONE
                    binding.engLastNameErrorTv.visibility = View.GONE
                }

            } else {
                binding.engLastNameClearIv.visibility = View.GONE
                binding.engLastNameLine.visibility = View.VISIBLE
                binding.engSelectedLastNameLine.visibility = View.GONE
                binding.engErrorLastNameLine.visibility = View.GONE
            }

        }
        binding.engFirstNameEt.doOnTextChanged { text, _, _, _ ->
            val input = text.toString()
            val engPattern = Regex("^[A-z]+$")

            val isEng = engPattern.matches(input)
            val isLengthEnough = input.length > 1
            val isValid = isEng && isLengthEnough


            if (input.isNotEmpty()) {
                binding.engFirstNameClearIv.visibility = View.VISIBLE
                if (isValid) {
                    binding.engFirstNameLine.visibility = View.GONE
                    binding.engErrorFirstNameLine.visibility = View.GONE
                    binding.engFirstNameErrorTv.visibility = View.GONE
                    binding.engSelectedFirstNameLine.visibility=View.GONE
                    binding.engFirstNameClearIv.visibility=View.GONE

                } else {
                    binding.engFirstNameClearIv.setImageResource(R.drawable.no_m)
                    binding.engFirstNameClearIv.isClickable = true

                }
                if (!isEng) {
                    binding.engSelectedFirstNameLine.visibility = View.GONE
                    binding.engErrorFirstNameLine.visibility = View.VISIBLE
                    binding.engFirstNameErrorTv.visibility = View.VISIBLE
                } else {
                    binding.engSelectedFirstNameLine.visibility = View.VISIBLE
                    binding.engErrorFirstNameLine.visibility = View.GONE
                    binding.engFirstNameErrorTv.visibility = View.GONE
                }

            } else {
                binding.engFirstNameClearIv.visibility = View.GONE
                binding.engFirstNameLine.visibility = View.VISIBLE
                binding.engSelectedFirstNameLine.visibility = View.GONE
                binding.engErrorFirstNameLine.visibility = View.GONE
            }

        }
        binding.koreanNameClearIv.setOnClickListener {
            binding.koreanNameEt.text.clear()
        }
        binding.engLastNameClearIv.setOnClickListener {
            binding.engLastNameEt.text.clear()
        }
        binding.engFirstNameClearIv.setOnClickListener {
            binding.engFirstNameEt.text.clear()
        }
        setupResidentNumberInput()

        binding.joinInputActiveCv.setOnClickListener {
            findNavController().navigate(R.id.action_joinFragment_to_joinAuthFragment)
        }
    }
    private fun setupResidentNumberInput() {
        binding.regNumRearEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
        binding.regNumFrontEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
        binding.regNumRearEt.transformationMethod = BigDotTransformationMethod()
        binding.regNumFrontEt.transformationMethod = BigDotTransformationMethod()
        binding.regEyeIv.visibility = View.INVISIBLE


        binding.regNumFrontEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.regNumFrontLine.visibility = View.INVISIBLE
                binding.regNumActiveFrontLine.visibility = View.VISIBLE
            } else {
                binding.regNumFrontLine.visibility = View.VISIBLE
                binding.regNumActiveFrontLine.visibility = View.INVISIBLE
            }
        }


        binding.regNumFrontEt.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.regEyeIv.visibility = View.INVISIBLE
            } else {
                binding.regEyeIv.visibility = View.VISIBLE
            }
            if (text?.length == 6) {
                binding.regNumRearEt.requestFocus()
            }
            checkInputComplete()
//            updateResidentNumberToViewModel()
        }

        binding.regNumRearEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.regNumRearLine.visibility = View.VISIBLE
                binding.regNumSelectedRearLine.visibility = View.INVISIBLE
            } else {
                binding.regNumRearLine.visibility = View.VISIBLE
                binding.regNumSelectedRearLine.visibility = View.INVISIBLE

            }
        }

        binding.regNumRearEt.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (binding.regNumRearEt.text.isNullOrEmpty()) {
                    binding.regNumFrontEt.requestFocus()
                    binding.regNumFrontEt.setSelection(binding.regNumFrontEt.length())
                    return@setOnKeyListener true
                }
            }
            false
        }

        binding.regNumRearEt.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.regEyeIv.visibility = View.VISIBLE
            } else {
                binding.regEyeIv.visibility = View.VISIBLE
                binding.joinInputActiveCv.visibility=View.VISIBLE
            }
            checkInputComplete()
        }

        binding.regEyeIv.setOnClickListener {
            isRearNumberVisible = !isRearNumberVisible

            if (isRearNumberVisible) {
                binding.regNumRearEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.regNumFrontEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.regEyeIv.alpha = 1.0f
                binding.regEyeIv.setImageResource(R.drawable.eye)
            } else {
                binding.regNumRearEt.transformationMethod = BigDotTransformationMethod()
                binding.regNumFrontEt.transformationMethod = BigDotTransformationMethod()
                binding.regEyeIv.alpha = 0.5f
                binding.regEyeIv.setImageResource(R.drawable.eye_off)
            }
            val length = binding.regNumRearEt.text?.length ?: 0
            binding.regNumRearEt.setSelection(length)
        }
    }
    class BigDotTransformationMethod : PasswordTransformationMethod() {
        override fun getTransformation(source: CharSequence, view: View): CharSequence {
            return BigDotCharSequence(source)
        }

        private class BigDotCharSequence(private val source: CharSequence) : CharSequence {
            override val length: Int
                get() = source.length

            override fun get(index: Int): Char {
                return '●'
            }

            override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
                return BigDotCharSequence(source.subSequence(startIndex, endIndex))
            }
        }
    }
    private fun checkInputComplete() {
        val front = binding.regNumFrontEt.text.toString()
        val rear = binding.regNumRearEt.text.toString()

        val fullResidentNumber = front + rear

        val isComplete = front.length == 6 && rear.length == 1

        if (isComplete) {
            //viewModel.updateResidentNumber(fullResidentNumber)

            binding.joinInputActiveCv.visibility = View.VISIBLE
            binding.joinInputInactiveCv.visibility = View.GONE
        } else {
            binding.joinInputActiveCv.visibility = View.GONE
            binding.joinInputInactiveCv.visibility = View.VISIBLE
        }
    }

}