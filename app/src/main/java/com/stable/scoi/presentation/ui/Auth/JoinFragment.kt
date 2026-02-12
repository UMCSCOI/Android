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
    private var isRearNumberVisible = true

    override fun initView() {
        binding.joinBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        // 1. 한국어 이름
        binding.koreanNameEt.setOnFocusChangeListener { _, hasFocus ->
            val input = binding.koreanNameEt.text.toString()
            binding.koreanNameClearIv.visibility = if (hasFocus && input.isNotEmpty()) View.VISIBLE else View.GONE
        }

        binding.koreanNameEt.doOnTextChanged { text, _, _, _ ->
            val input = text.toString()
            val hasFocus = binding.koreanNameEt.hasFocus()

            binding.koreanNameClearIv.visibility = if (input.isNotEmpty() && hasFocus) View.VISIBLE else View.GONE

            if (input.isEmpty()) {
                binding.koreanNameLine.visibility = View.VISIBLE
                binding.koreanSelectedNameLine.visibility = View.GONE
                ErrorKorean(false)
            } else {
                val koreanPattern = Regex("^[가-힣ㄱ-ㅎㅏ-ㅣ]+$")
                val isKorean = koreanPattern.matches(input)
                val isLengthEnough = input.length > 1

                if (isKorean && isLengthEnough) {
                    binding.koreanNameLine.visibility = View.GONE
                    binding.koreanSelectedNameLine.visibility = View.VISIBLE
                    ErrorKorean(false)
                    viewModel.onKoreanNameChanged(input)
                } else {
                    binding.koreanNameLine.visibility = View.GONE
                    binding.koreanSelectedNameLine.visibility = View.GONE
                    ErrorKorean(true)
                }
            }
            checkAllInputComplete()
        }

        // 2. 영어 성 (Last Name)
        binding.engLastNameEt.setOnFocusChangeListener { _, hasFocus ->
            val input = binding.engLastNameEt.text.toString()
            if (hasFocus && input.isNotEmpty()) {
                binding.engLastNameClearIv.visibility = View.VISIBLE
                binding.engSelectedFirstNameLine.visibility = View.VISIBLE
            } else {
                binding.engLastNameClearIv.visibility = View.GONE
                binding.engSelectedFirstNameLine.visibility = View.GONE
            }
        }

        binding.engLastNameEt.doOnTextChanged { text, _, _, _ ->
            val input = text.toString()

            if (input != input.uppercase()) {
                val upperCaseText = input.uppercase()
                binding.engLastNameEt.setText(upperCaseText)
                binding.engLastNameEt.setSelection(upperCaseText.length)
                return@doOnTextChanged
            }

            val hasFocus = binding.engLastNameEt.hasFocus()
            val engPattern = Regex("^[A-Z]+$") // 대문자만 허용
            val isEng = engPattern.matches(input)
            val isLengthEnough = input.length > 1
            val isValid = isEng && isLengthEnough

            binding.engLastNameClearIv.visibility = if (hasFocus && input.isNotEmpty()) View.VISIBLE else View.GONE

            if (input.isNotEmpty()) {
                if (isValid) {
                    binding.engLastNameLine.visibility = View.VISIBLE
                    ErrorEngLastName(false)
                } else {
                    ErrorEngLastName(true)
                }
            } else {
                binding.engLastNameLine.visibility = View.VISIBLE
                ErrorEngLastName(false)
            }
            combineEnglishName()
            checkAllInputComplete()
        }

        // 3. 영어 이름 (First Name)
        binding.engFirstNameEt.setOnFocusChangeListener { _, hasFocus ->
            val input = binding.engFirstNameEt.text.toString()
            if (hasFocus && input.isNotEmpty()) {
                binding.engFirstNameClearIv.visibility = View.VISIBLE
                binding.engSelectedFirstNameLine.visibility = View.VISIBLE
            } else {
                binding.engFirstNameClearIv.visibility = View.GONE
                binding.engSelectedFirstNameLine.visibility = View.GONE
            }
        }

        binding.engFirstNameEt.doOnTextChanged { text, _, _, _ ->
            val input = text.toString()

            if (input != input.uppercase()) {
                val upperCaseText = input.uppercase()
                binding.engFirstNameEt.setText(upperCaseText)
                binding.engFirstNameEt.setSelection(upperCaseText.length)
                return@doOnTextChanged
            }

            val hasFocus = binding.engFirstNameEt.hasFocus()
            val engPattern = Regex("^[A-Z]+$")
            val isEng = engPattern.matches(input)
            val isLengthEnough = input.length > 1
            val isValid = isEng && isLengthEnough

            binding.engFirstNameClearIv.visibility = if (hasFocus && input.isNotEmpty()) View.VISIBLE else View.GONE

            if (input.isNotEmpty()) {
                if (isValid) {
                    binding.engFirstNameLine.visibility = View.VISIBLE
                    ErrorEngFirstName(false)
                } else {
                    ErrorEngFirstName(true)
                }
            } else {
                binding.engFirstNameLine.visibility = View.VISIBLE
                ErrorEngFirstName(false)
            }
            combineEnglishName()
            checkAllInputComplete()
        }


        binding.koreanNameClearIv.setOnClickListener { binding.koreanNameEt.text.clear() }
        binding.engLastNameClearIv.setOnClickListener { binding.engLastNameEt.text.clear() }
        binding.engFirstNameClearIv.setOnClickListener { binding.engFirstNameEt.text.clear() }

        setupResidentNumberInput()

        binding.joinInputActiveCv.setOnClickListener {
          findNavController().navigate(R.id.action_joinFragment_to_loginRegFragment)
        }

        checkAllInputComplete()
    }

    private fun combineEnglishName() {
        val lastName = binding.engLastNameEt.text.toString().trim()
        val firstName = binding.engFirstNameEt.text.toString().trim()
        val fullName = "$lastName $firstName"
        viewModel.onEnglishNameChanged(fullName)
    }

    private fun setupResidentNumberInput() {
        binding.regNumRearEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
        binding.regNumFrontEt.transformationMethod = HideReturnsTransformationMethod.getInstance()

        // 눈 아이콘 초기화
        binding.regEyeIv.setImageResource(R.drawable.eye_off)
        binding.regEyeIv.alpha = 1.0f
        binding.regEyeIv.visibility = View.INVISIBLE

        // Front EditText
        binding.regNumFrontEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.regNumFrontLine.visibility = View.INVISIBLE
                binding.regNumActiveFrontLine.visibility = View.VISIBLE
                if (!binding.regNumFrontEt.text.isNullOrEmpty()) binding.regEyeIv.visibility = View.VISIBLE
            } else {
                binding.regNumFrontLine.visibility = View.VISIBLE
                binding.regNumActiveFrontLine.visibility = View.INVISIBLE
                binding.regEyeIv.visibility = View.INVISIBLE
            }
        }

        binding.regNumFrontEt.doOnTextChanged { text, _, _, _ ->
            binding.regEyeIv.visibility = if (text.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE

            if (text?.length == 6) {
                binding.regNumRearEt.requestFocus()
            }
            checkAllInputComplete()
        }

        binding.regNumRearEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.regNumRearLine.visibility = View.VISIBLE
                binding.regNumSelectedRearLine.visibility = View.INVISIBLE
                binding.regEyeIv.visibility = View.VISIBLE
            } else {
                binding.regNumRearLine.visibility = View.VISIBLE
                binding.regNumSelectedRearLine.visibility = View.INVISIBLE
                binding.regEyeIv.visibility = View.INVISIBLE
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

        binding.regNumRearEt.doOnTextChanged { _, _, _, _ ->
            binding.regEyeIv.visibility = View.VISIBLE
            checkAllInputComplete()
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
            override val length: Int get() = source.length
            override fun get(index: Int): Char = '●'
            override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
                return BigDotCharSequence(source.subSequence(startIndex, endIndex))
            }
        }
    }

    private fun ErrorKorean(Error: Boolean) {
        binding.koreanErrorNameLine.visibility = if (Error) View.VISIBLE else View.GONE
        binding.koreanErrorTv.visibility = if (Error) View.VISIBLE else View.GONE
    }

    private fun ErrorEngLastName(Error: Boolean) {
        binding.engErrorLastNameLine.visibility = if (Error) View.VISIBLE else View.GONE
        binding.engLastNameErrorTv.visibility = if (Error) View.VISIBLE else View.GONE
    }

    private fun ErrorEngFirstName(Error: Boolean) {
        binding.engErrorFirstNameLine.visibility = if (Error) View.VISIBLE else View.GONE
        binding.engFirstNameErrorTv.visibility = if (Error) View.VISIBLE else View.GONE
    }

    private fun checkAllInputComplete() {
        val koreanName = binding.koreanNameEt.text.toString()
        val engLastName = binding.engLastNameEt.text.toString()
        val engFirstName = binding.engFirstNameEt.text.toString()
        val resFront = binding.regNumFrontEt.text.toString()
        val resRear = binding.regNumRearEt.text.toString()

        val isKoreanValid = koreanName.isNotEmpty() && koreanName.matches(Regex("^[가-힣ㄱ-ㅎㅏ-ㅣ]+$"))
        val isEngLastValid = engLastName.isNotEmpty() && engLastName.matches(Regex("^[A-Z]+$"))
        val isEngFirstValid = engFirstName.isNotEmpty() && engFirstName.matches(Regex("^[A-Z]+$"))
        val isResValid = resFront.length == 6 && resRear.length == 1

        val isAllComplete = isKoreanValid && isEngLastValid && isEngFirstValid && isResValid

        if (isAllComplete) {
            binding.joinInputActiveCv.visibility = View.VISIBLE
            binding.joinInputInactiveCv.visibility = View.GONE
            viewModel.updateResidentNumber(resFront + resRear)
        } else {
            binding.joinInputActiveCv.visibility = View.GONE
            binding.joinInputInactiveCv.visibility = View.VISIBLE
        }
    }
}