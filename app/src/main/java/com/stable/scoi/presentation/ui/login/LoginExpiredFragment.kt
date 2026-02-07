package com.stable.scoi.presentation.ui.login

import android.os.CountDownTimer
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentLoginExpiredBinding
import com.stable.scoi.extension.toPhoneNumber

import com.stable.scoi.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginExpiredFragment :
    BaseFragment<FragmentLoginExpiredBinding, LoginState, LoginEvent, LoginViewModel>(
        FragmentLoginExpiredBinding::inflate
    ) {

    override val viewModel: LoginViewModel by activityViewModels()
    private var countDownTimer: CountDownTimer? = null

    // 현재 상태가 '인증번호 전송 전'인지 '후(코드 입력 중)'인지 구분
    private var isCodeSent: Boolean = false
    private var code:String=""
    override fun initView() {
        observeEvents()

        // 초기화
        viewModel.onPinChanged("")
        inputUi(isActive = false)

        binding.loginExpiredPhoneAuthNumberEt.doOnTextChanged { text, _, _, _ ->
            val input = text.toString()
            val rawNumber = input.replace("-", "") // 하이픈 제거한 숫자만

            binding.loginExpiredPhoneAuthNumberClearIv.visibility = View.VISIBLE
            binding.loginExpiredPhoneAuthSelectedNumberLine.visibility = View.VISIBLE

            // 11자리가 다 입력되었을 때
            if (rawNumber.length == 11) {
                val formatted = input.toPhoneNumber()

                if (input != formatted) {
                    binding.loginExpiredPhoneAuthNumberEt.setText(formatted)
                    binding.loginExpiredPhoneAuthNumberEt.setSelection(formatted.length) // 커서 맨 뒤로
                }

                viewModel.onPhoneAuthNumberChanged(rawNumber)

                if (!isCodeSent) {
                    inputUi(isActive = true)
                    binding.loginExpiredPhoneAuthNumberSendTv.visibility = View.VISIBLE
                }
            } else {
                inputUi(isActive = false)
                binding.loginExpiredPhoneAuthNumberSendTv.visibility = View.GONE
            }
        }


        binding.loginExpiredPhoneAuthCodeEt.doOnTextChanged { text, _, _, _ ->
            binding.loginExpiredPhoneAuthErrorTv.visibility = View.GONE
            val input = text.toString()

            binding.loginExpiredPhoneAuthCodeClearIv.visibility = View.VISIBLE
            binding.loginExpiredPhoneAuthSelectedCodeLine.visibility = View.VISIBLE
            binding.loginExpiredPhoneAuthHelperTv.visibility=View.VISIBLE

            // 6자리 입력 완료 시123
            if (input.length == 6) {
                viewModel.onAuthChanged(input)
                inputUi(isActive = true) // 확인 버튼 활성화
                code=input
            } else {
                inputUi(isActive = false) // 확인 버튼 비활성화
            }
        }

        binding.loginExpiredPhoneAuthInputActiveCv.setOnClickListener {
            if (!isCodeSent) {
                //TODO 인증번호 전송
            } else {
                onSendButtonClicked()
                viewModel.onAuthChanged(code) // 뷰모델의 확인 함수 호출

            }
        }

        binding.loginExpiredPhoneAuthNumberSendTv.setOnClickListener {
            onSendButtonClicked()
        }

        binding.loginExpiredPhoneAuthCodeRetryTv.setOnClickListener {
           onSendButtonClicked()
        }
    }

    private fun onSendButtonClicked() {
        isCodeSent = true

        // 1. 뷰모델 요청
        viewModel.onSendClicked() // 실제 전송 요청 (함수명 확인 필요)

        // 2. UI 변경
        binding.loginExpiredPhoneAuthNumberSendTv.visibility = View.GONE
        binding.loginExpiredPhoneAuthNumberRetryTv.visibility = View.VISIBLE
        binding.loginExpiredPhoneAuthHelperTv.visibility = View.VISIBLE

        // 3. 타이머 시작
        startTimer()

        binding.loginExpiredPhoneAuthCodeEt.requestFocus()
        inputUi(isActive = false) // 인증번호 입력 전까지 버튼 다시 비활성화
    }

    // 버튼 색상 변경 (활성/비활성)
    private fun inputUi(isActive: Boolean) {
        if (isActive) {
            binding.loginExpiredPhoneAuthInputActiveCv.visibility = View.VISIBLE
            binding.loginExpiredPhoneAuthInputInactiveCv.visibility = View.GONE
        } else {
            binding.loginExpiredPhoneAuthInputActiveCv.visibility = View.GONE
            binding.loginExpiredPhoneAuthInputInactiveCv.visibility = View.VISIBLE
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

    private fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.VerifySuccess -> {
                countDownTimer?.cancel() // 타이머 종료
                binding.loginExpiredPhoneAuthCodeActiveTimerTv.visibility= View.GONE
                binding.loginExpiredPhoneAuthCodeActiveCheckIv.visibility=View.VISIBLE
                binding.loginExpiredPhoneAuthCodeActiveCheckIv.visibility = View.VISIBLE
                binding.loginExpiredPhoneAuthCodeCheckIv.visibility = View.GONE

                // 화면 이동
                findNavController().navigate(R.id.action_loginExpiredFragment_to_loginFragment)
            }
            is LoginEvent.ShowError -> {
                binding.loginExpiredPhoneAuthHelperTv.visibility=View.GONE
                binding.loginExpiredPhoneAuthErrorTv.visibility = View.VISIBLE
                inputUi(isActive = false)
            }
            // 나머지 이벤트들 처리 (필요시 구현)
            else -> {}
        }
    }

    private fun startTimer() {
        binding.loginExpiredPhoneAuthCodeActiveTimerTv.visibility = View.VISIBLE
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.loginExpiredPhoneAuthCodeActiveTimerTv.text =
                    String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.loginExpiredPhoneAuthCodeActiveTimerTv.text = "00:00"
                binding.loginExpiredPhoneAuthCodeRetryTv.visibility = View.VISIBLE
                binding.loginExpiredPhoneAuthHelperTv.visibility = View.GONE
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