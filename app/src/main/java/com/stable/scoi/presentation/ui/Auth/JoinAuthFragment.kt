package com.stable.scoi.presentation.ui.Auth

import android.os.CountDownTimer
import android.view.View
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
class JoinAuthFragment : BaseFragment<FragmentPhoneAuthBinding, JoinState, JoinEvent, JoinViewModel>(
    FragmentPhoneAuthBinding::inflate
) {
    override val viewModel: JoinViewModel by activityViewModels()
    private var countDownTimer: CountDownTimer? = null

    private var isCodeSent: Boolean = false
    private var code:String=""
    override fun initView() {

        binding.phoneAuthBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        observeEvents()
        inputUi(isActive = false)

        binding.phoneAuthNumberEt.doOnTextChanged { text, _, _, _ ->
            val input = text.toString()
            val rawNumber = input.replace("-", "")

            binding.phoneAuthSelectedNumberLine.visibility = View.VISIBLE

            if (rawNumber.length == 11) {
                val formatted = input.toPhoneNumber()

                if (input != formatted) {
                    binding.phoneAuthNumberEt.setText(formatted)
                    binding.phoneAuthNumberEt.setSelection(formatted.length)
                }

                viewModel.onPhoneAuthNumberChanged(rawNumber)

                if (!isCodeSent) {
                    inputUi(isActive = true)
                    binding.phoneAuthNumberSendTv.visibility = View.VISIBLE
                }
            } else {
                inputUi(isActive = false)
                binding.phoneAuthNumberSendTv.visibility = View.GONE
            }
        }


        binding.phoneAuthCodeEt.doOnTextChanged { text, _, _, _ ->
            binding.phoneAuthErrorTv.visibility = View.GONE
            val input = text.toString()

            binding.phoneAuthCodeCheckIv.visibility = View.VISIBLE
            binding.phoneAuthSelectedCodeLine.visibility = View.VISIBLE
            binding.phoneAuthHelperTv.visibility=View.VISIBLE

            if (input.length == 6) {
                viewModel.onAuthChanged(input)
                inputUi(isActive = true)
                code=input
            } else {
                inputUi(isActive = false)
            }
        }

        binding.phoneAuthInputActiveCv.setOnClickListener {
            if (!isCodeSent) {
                //TODO 인증번호 전송
            } else {
                onSendButtonClicked()
                viewModel.onAuthChanged(code)

            }
        }

        binding.phoneAuthNumberSendTv.setOnClickListener {
            onSendButtonClicked()
        }

        binding.phoneAuthCodeRetryTv.setOnClickListener {
            onSendButtonClicked()
        }
    }

    private fun onSendButtonClicked() {
        isCodeSent = true

        viewModel.onSendClicked()
        binding.phoneAuthNumberSendTv.visibility = View.GONE
        binding.phoneAuthNumberRetryTv.visibility = View.VISIBLE
        binding.phoneAuthHelperTv.visibility = View.VISIBLE

        startTimer()

        binding.phoneAuthCodeEt.requestFocus()
        inputUi(isActive = false)
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
                countDownTimer?.cancel() // 타이머 종료
                binding.phoneAuthCodeActiveTimerTv.visibility= View.GONE
                binding.phoneAuthCodeActiveCheckIv.visibility=View.VISIBLE
                binding.phoneAuthCodeActiveCheckIv.visibility = View.VISIBLE
                binding.phoneAuthCodeCheckIv.visibility = View.GONE

               findNavController().navigate(R.id.action_joinAuthFragment_to_loginRegFragment)
            }
            is JoinEvent.ShowError -> {
                binding.phoneAuthHelperTv.visibility=View.GONE
                binding.phoneAuthErrorTv.visibility = View.VISIBLE
                inputUi(isActive = false)
            }
            else -> {}
        }
    }

    private fun startTimer() {
        binding.phoneAuthCodeActiveTimerTv.visibility = View.VISIBLE
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(300000, 1000) {
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
            }
        }
        countDownTimer?.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        countDownTimer = null
    }
}
