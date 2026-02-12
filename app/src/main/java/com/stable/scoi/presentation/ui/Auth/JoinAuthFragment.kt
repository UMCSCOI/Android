package com.stable.scoi.presentation.ui.Auth

import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentPhoneAuthBinding
import com.stable.scoi.extension.toPhoneNumber
import com.stable.scoi.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JoinAuthFragment :
    BaseFragment<FragmentPhoneAuthBinding, JoinState, JoinEvent, JoinViewModel>(
        FragmentPhoneAuthBinding::inflate
    ) {
    override val viewModel: JoinViewModel by activityViewModels()
    private var authTimer: CountDownTimer? = null
    private var resendTimer: CountDownTimer? = null
    private var isCodeSent: Boolean = false
    private var code: String = ""

    override fun initView() {

        binding.phoneAuthBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        observeEvents()
        inputUi(isActive = false)

        binding.phoneAuthNumberEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.phoneAuthInputInactiveCv.visibility = View.INVISIBLE
            } else {
                binding.phoneAuthInputInactiveCv.visibility = View.VISIBLE
            }
        }
        binding.phoneAuthCodeEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.phoneAuthInputInactiveCv.visibility = View.INVISIBLE
            } else {
                binding.phoneAuthInputInactiveCv.visibility = View.VISIBLE
            }
        }

        // 전화번호 입력 로직
        binding.phoneAuthNumberEt.doOnTextChanged { text, _, _, _ ->
            val input = text.toString()
            val rawNumber = input.replace("-", "")

            binding.phoneAuthSelectedNumberLine.visibility = View.VISIBLE

            if (rawNumber.length == 11) {
                val formatted = input.toPhoneNumber()

                if (input != formatted) {
                    binding.phoneAuthNumberEt.setText(formatted)
                    binding.phoneAuthNumberEt.setSelection(formatted.length)
                    return@doOnTextChanged
                }

                viewModel.onPhoneAuthNumberChanged(rawNumber)
                binding.phoneAuthNumberSendTv.visibility = View.VISIBLE

            } else {
                binding.phoneAuthNumberSendTv.visibility = View.GONE
                inputUi(isActive = false)
            }
        }

        // 인증번호 입력 로직
        binding.phoneAuthCodeEt.doOnTextChanged { text, _, _, _ ->
            binding.phoneAuthErrorTv.visibility = View.GONE
            val input = text.toString()

            binding.phoneAuthCodeCheckIv.visibility = View.VISIBLE
            binding.phoneAuthSelectedCodeLine.visibility = View.VISIBLE
            binding.phoneAuthHelperTv.visibility = View.VISIBLE

            if (input.length == 6) {
                viewModel.onAuthCodeChanged(input)
                inputUi(isActive = true)
                code = input
            } else {
                inputUi(isActive = false)
            }
        }

        binding.phoneAuthInputActiveCv.setOnClickListener {
            if (!isCodeSent) {
                onSendButtonClicked()
            } else {
                viewModel.verifySms()
            }
        }

        binding.phoneAuthNumberSendTv.setOnClickListener {
            onSendButtonClicked()
        }

        binding.phoneAuthCodeRetryTv.setOnClickListener {
            onSendButtonClicked()
        }
    }

    private var isFirstSend = true

    private fun onSendButtonClicked() {
        isCodeSent = true
        viewModel.sendSms()

        if (isFirstSend) {
            binding.phoneAuthNumberSendTv.text = "재발송"
            isFirstSend = false
            binding.phoneAuthNumberSendTv.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.sub_gray_2
                )
            )

        }

        binding.phoneAuthHelperTv.visibility = View.VISIBLE


        startAuthTimer()
        startResendCooldown()

        binding.phoneAuthCodeEt.requestFocus()

        inputUi(isActive = false)
    }

    private fun startResendCooldown() {
        resendTimer?.cancel()

        binding.phoneAuthNumberSendTv.isEnabled = false
        binding.phoneAuthNumberSendTv.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.sub_gray_2
            )
        )

        resendTimer = object : CountDownTimer(60000, 1000) { // 60초
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                binding.phoneAuthNumberSendTv.isEnabled = true
                binding.phoneAuthNumberSendTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.active
                    )
                )
            }
        }
        resendTimer?.start()
    }

    private fun startAuthTimer() {
        binding.phoneAuthCodeActiveTimerTv.visibility = View.VISIBLE
        authTimer?.cancel()

        authTimer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.phoneAuthCodeActiveTimerTv.text =
                    String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.phoneAuthCodeActiveTimerTv.text = "00:00"
                binding.phoneAuthCodeRetryTv.visibility = View.VISIBLE
                binding.phoneAuthHelperTv.visibility = View.GONE

                isCodeSent = false
                inputUi(isActive = false)
            }
        }
        authTimer?.start()
    }

    private fun inputUi(isActive: Boolean) {
        if (isActive) {
            binding.phoneAuthInputActiveCv.visibility = View.VISIBLE
            binding.phoneAuthInputInactiveCv.visibility = View.GONE
        } else {
            binding.phoneAuthInputActiveCv.visibility = View.GONE
            binding.phoneAuthInputInactiveCv.visibility = View.VISIBLE
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { event ->
                    handleEvent(event)
                }
            }
        }
    }

    private fun handleEvent(event: JoinEvent) {
        when (event) {
            is JoinEvent.VerifySuccess -> {
                authTimer?.cancel()
                resendTimer?.cancel()

                binding.phoneAuthCodeActiveTimerTv.visibility = View.GONE
                binding.phoneAuthCodeActiveCheckIv.visibility = View.VISIBLE
                binding.phoneAuthCodeCheckIv.visibility = View.GONE

                findNavController().navigate(R.id.action_joinAuthFragment_to_joinFragment)
            }

            is JoinEvent.ShowError -> {
                binding.phoneAuthHelperTv.visibility = View.GONE
                binding.phoneAuthErrorTv.visibility = View.VISIBLE
                inputUi(isActive = false)
            }

            else -> {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        authTimer?.cancel()
        authTimer = null
        resendTimer?.cancel()
        resendTimer = null
    }
}