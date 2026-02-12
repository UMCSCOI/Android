package com.stable.scoi.presentation.ui.bio

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentBioLoginBinding
import com.stable.scoi.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

@AndroidEntryPoint
class BioFragment : BaseFragment<FragmentBioLoginBinding, BioState, BioEvent, BioViewModel>(
    FragmentBioLoginBinding::inflate
){
    override val viewModel: BioViewModel by activityViewModels()

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val handler = Handler(Looper.getMainLooper())

    var count:Int=0
    override fun initView() {
        observeEvents()



        // 1. 실행자 초기화
        executor = ContextCompat.getMainExecutor(requireContext())

        // 2. 콜백 정의
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)


                    binding.loginBioSuccessIv.visibility = View.VISIBLE
                    binding.loginBioTouchIv.visibility = View.GONE
                    binding.loginBioErrorIv.visibility = View.GONE
                    binding.loginBioPinTv.visibility=View.GONE
                    binding.loginBioUncheckIv.visibility= View.GONE
                    handler.postDelayed({
                        viewModel.onBiometricSuccess()
                    },2000)


                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    binding.loginBioTouchErrorIv.visibility=View.VISIBLE
                    binding.loginBioUncheckIv.visibility=View.GONE
                    binding.loginBioPinTv.visibility=View.GONE
                    viewModel.onCountOver(count)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    binding.loginBioTouchErrorIv.visibility = View.VISIBLE
                    binding.loginBioUncheckIv.visibility = View.GONE
                    binding.loginBioSuccessIv.visibility = View.GONE
                    binding.loginBioPinTv.visibility=View.GONE
                    viewModel.onCountOver(count)
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("생체 인증 로그인")
            .setSubtitle("등록된 지문으로 로그인해주세요.")
            .setNegativeButtonText("취소")
            .build()

        binding.loginBioPinTv.setOnClickListener {
            findNavController().navigate(R.id.action_bioFragment_to_loginFragment)
        }

        binding.loginBioUncheckIv.setOnClickListener {
            count+=1
            biometricPrompt.authenticate(promptInfo)
        }
        binding.loginBioTouchErrorIv.setOnClickListener {
            count+=1
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun handleEvent(event: BioEvent) {
        when(event){
            is BioEvent.NavigationToMain -> {
                findNavController().navigate(R.id.action_bioFragment_to_homeFragment)
            }
            is BioEvent.NavigationToPin -> {
                findNavController().navigate(R.id.action_bioFragment_to_loginFragment)
            }
            else -> {}
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // BaseViewModel에서 사용하는 변수명(uiEvent, event, eventFlow 등)으로 맞춰주세요
                viewModel.uiEvent.collect { event ->
                    handleEvent(event)
                }
            }
        }
    }

    private fun resetUI() {
        binding.loginBioUncheckIv.visibility = View.VISIBLE
        binding.loginBioTouchErrorIv.visibility = View.GONE
        binding.loginBioSuccessIv.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
            checkStatusAndAction()
    }

    private fun checkStatusAndAction() {
        val biometricManager = BiometricManager.from(requireContext())

        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {

            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                showLoginExpiredDialog()
            }
            else -> {
            }
        }
    }

    private fun showLoginExpiredDialog() {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setContentView(R.layout.dialog_no_biometric)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.setCancelable(false)

        dialog.findViewById<TextView>(R.id.no_biometric_reg_tv)?.setOnClickListener {
            dialog.dismiss()

            findNavController().navigate(R.id.action_bioFragment_to_bioRegFragment)
        }

        dialog.findViewById<TextView>(R.id.no_biometric_close_tv)?.setOnClickListener {
            dialog.dismiss()

            findNavController().navigate(R.id.action_bioFragment_to_loginFragment)
        }

        dialog.show()
    }

}