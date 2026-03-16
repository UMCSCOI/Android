package com.stable.scoi.presentation.ui.Auth

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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
    private lateinit var allEditTexts: List<EditText>

    override fun initView() {
        allEditTexts = listOf(
            binding.koreanNameEt, binding.engLastNameEt,
            binding.engFirstNameEt, binding.regNumFrontEt, binding.regNumRearEt
        )

        binding.joinBackBtn.setOnClickListener { findNavController().popBackStack() }

        val white = ContextCompat.getColor(requireActivity(), R.color.white)
        requireActivity().findViewById<View>(R.id.main).setBackgroundColor(white)

        setupFocusListeners()
        setupTextChangeListeners()
        setupResidentNumberInput()

        binding.koreanNameClearIv.setOnClickListener { binding.koreanNameEt.text.clear() }
        binding.engLastNameClearIv.setOnClickListener { binding.engLastNameEt.text.clear() }
        binding.engFirstNameClearIv.setOnClickListener { binding.engFirstNameEt.text.clear() }

        binding.joinInputActiveCv.setOnClickListener {
            findNavController().navigate(R.id.action_joinFragment_to_loginRegFragment)
        }
    }

    private fun setupFocusListeners() {
        allEditTexts.forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                val input = editText.text.toString()

                when (editText.id) {
                    R.id.korean_name_et -> {
                        binding.koreanNameClearIv.visibility = if (hasFocus && input.isNotEmpty()) View.VISIBLE else View.GONE
                        binding.koreanNameLine.visibility = if (hasFocus) View.INVISIBLE else View.VISIBLE
                        binding.koreanSelectedNameLine.visibility = if (hasFocus) View.VISIBLE else View.INVISIBLE
                    }
                    R.id.eng_last_name_et -> {
                        binding.engLastNameClearIv.visibility = if (hasFocus && input.isNotEmpty()) View.VISIBLE else View.GONE
                        binding.engLastNameLine.visibility = if (hasFocus) View.INVISIBLE else View.VISIBLE
                        binding.engSelectedLastNameLine.visibility = if (hasFocus) View.VISIBLE else View.INVISIBLE
                    }
                    R.id.eng_first_name_et -> {
                        binding.engFirstNameClearIv.visibility = if (hasFocus && input.isNotEmpty()) View.VISIBLE else View.GONE
                        binding.engFirstNameLine.visibility = if (hasFocus) View.INVISIBLE else View.VISIBLE
                        binding.engSelectedFirstNameLine.visibility = if (hasFocus) View.VISIBLE else View.INVISIBLE
                    }
                }
                checkAllInputComplete()
            }
        }
    }

    private fun setupTextChangeListeners() {
        binding.koreanNameEt.doOnTextChanged { text, _, _, _ ->
            val input = text.toString()
            binding.koreanNameClearIv.visibility = if (input.isNotEmpty() && binding.koreanNameEt.hasFocus()) View.VISIBLE else View.GONE

            if (input.isEmpty()) {
                ErrorKorean(false)
            } else {
                val isKorean = Regex("^[가-힣ㄱ-ㅎㅏ-ㅣ]+$").matches(input)
                if (isKorean && input.length > 1) {
                    ErrorKorean(false)
                    viewModel.onKoreanNameChanged(input)
                } else {
                    ErrorKorean(true)
                }
            }
            checkAllInputComplete()
        }

        binding.engLastNameEt.doOnTextChanged { text, _, _, _ ->
            val input = text.toString()
            if (input != input.uppercase()) {
                binding.engLastNameEt.setText(input.uppercase())
                binding.engLastNameEt.setSelection(input.length)
                return@doOnTextChanged
            }
            ErrorEngLastName(!(input.isEmpty() || (Regex("^[A-Z]+$").matches(input) && input.length > 1)))
            combineEnglishName()
            checkAllInputComplete()
        }

        binding.engFirstNameEt.doOnTextChanged { text, _, _, _ ->
            val input = text.toString()
            if (input != input.uppercase()) {
                binding.engFirstNameEt.setText(input.uppercase())
                binding.engFirstNameEt.setSelection(input.length)
                return@doOnTextChanged
            }
            ErrorEngFirstName(!(input.isEmpty() || (Regex("^[A-Z]+$").matches(input) && input.length > 1)))
            combineEnglishName()
            checkAllInputComplete()
        }
    }

    private fun setupResidentNumberInput() {
        isRearNumberVisible = true
        val bigDotMethod = BigDotTransformationMethod()

        binding.regNumFrontEt.transformationMethod = bigDotMethod
        binding.regNumRearEt.transformationMethod = bigDotMethod

        binding.regEyeIv.setImageResource(R.drawable.eye)
        binding.regEyeIv.alpha = 1.0f
        binding.regEyeIv.visibility = View.INVISIBLE

        binding.regNumFrontEt.setOnFocusChangeListener { _, hasFocus ->
            binding.regNumFrontLine.visibility = if (hasFocus) View.INVISIBLE else View.VISIBLE
            binding.regNumActiveFrontLine.visibility = if (hasFocus) View.VISIBLE else View.INVISIBLE
            binding.regEyeIv.visibility = if (hasFocus && !binding.regNumFrontEt.text.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
            checkAllInputComplete()
        }

        binding.regNumFrontEt.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 6) binding.regNumRearEt.requestFocus()
            checkAllInputComplete()
        }

        binding.regNumRearEt.setOnFocusChangeListener { _, hasFocus ->
            binding.regNumRearLine.visibility = if (hasFocus) View.INVISIBLE else View.VISIBLE
            binding.regNumSelectedRearLine.visibility = if (hasFocus) View.VISIBLE else View.INVISIBLE
            binding.regEyeIv.visibility = if (hasFocus) View.VISIBLE else View.INVISIBLE
            checkAllInputComplete()
        }

        binding.regNumRearEt.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && binding.regNumRearEt.text.isNullOrEmpty()) {
                binding.regNumFrontEt.requestFocus()
                binding.regNumFrontEt.setSelection(binding.regNumFrontEt.length())
                return@setOnKeyListener true
            }
            false
        }

        binding.regNumRearEt.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 1) {
                hideKeyboard()
                binding.regNumRearEt.clearFocus()
            }
            checkAllInputComplete()
        }

        binding.regEyeIv.setOnClickListener {
            isRearNumberVisible = !isRearNumberVisible

            val method = if (isRearNumberVisible) BigDotTransformationMethod() else HideReturnsTransformationMethod.getInstance()
            binding.regNumRearEt.transformationMethod = method
            binding.regNumFrontEt.transformationMethod = method
            binding.regEyeIv.setImageResource(if (isRearNumberVisible) R.drawable.eye else R.drawable.eye_off)
            binding.regEyeIv.alpha = if (isRearNumberVisible) 1.0f else 0.5f

            binding.regNumRearEt.setSelection(binding.regNumRearEt.text?.length ?: 0)
            binding.regNumFrontEt.setSelection(binding.regNumFrontEt.text?.length ?: 0)
        }
    }

    private fun combineEnglishName() {
        val lastName = binding.engLastNameEt.text.toString().trim()
        val firstName = binding.engFirstNameEt.text.toString().trim()
        viewModel.onEnglishNameChanged("$lastName $firstName")
    }

    private fun checkAllInputComplete() {
        val koreanName = binding.koreanNameEt.text.toString()
        val engLastName = binding.engLastNameEt.text.toString()
        val engFirstName = binding.engFirstNameEt.text.toString()
        val resFront = binding.regNumFrontEt.text.toString()
        val resRear = binding.regNumRearEt.text.toString()

        val isKoreanValid = Regex("^[가-힣ㄱ-ㅎㅏ-ㅣ]+$").matches(koreanName) && koreanName.length > 1
        val isEngLastValid = Regex("^[A-Z]+$").matches(engLastName) && engLastName.length > 1
        val isEngFirstValid = Regex("^[A-Z]+$").matches(engFirstName) && engFirstName.length > 1
        val isResValid = resFront.length == 6 && resRear.length == 1

        val isAllComplete = isKoreanValid && isEngLastValid && isEngFirstValid && isResValid
        val isAnyFocused = allEditTexts.any { it.hasFocus() }

        if (isAnyFocused) {
            binding.joinInputActiveCv.visibility = View.GONE
            binding.joinInputInactiveCv.visibility = View.GONE
        } else {
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

    private fun hideKeyboard() {
        val window = requireActivity().window
        WindowInsetsControllerCompat(window, binding.root).hide(WindowInsetsCompat.Type.ime())
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

    class BigDotTransformationMethod : PasswordTransformationMethod() {
        override fun getTransformation(source: CharSequence, view: View): CharSequence = BigDotCharSequence(source)
        private class BigDotCharSequence(private val source: CharSequence) : CharSequence {
            override val length: Int get() = source.length
            override fun get(index: Int): Char = '●'
            override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = BigDotCharSequence(source.subSequence(startIndex, endIndex))
        }
    }
}