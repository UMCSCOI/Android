package com.stable.scoi.presentation.ui.bio

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentBioScanBinding
import com.stable.scoi.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class BioRegFragment : BaseFragment<FragmentBioScanBinding, BioState, BioEvent, BioViewModel>(
    FragmentBioScanBinding::inflate
){
    override val viewModel: BioViewModel by activityViewModels()

    private val handler = Handler(Looper.getMainLooper())

    private var waitingForResult = false

    override fun initView() {
        binding.bioScanBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.bioScanInputInactiveCv.setOnClickListener {
            findNavController().navigate(R.id.action_bioRegFragment_to_pinFragment)
        }

        binding.loginPinInputActiveCv.setOnClickListener {
            checkStatusAndAction()
        }
    }



    override fun onResume() {
        super.onResume()
        if (waitingForResult) {
            checkStatusAndAction()
        }
    }

//    private val args: BioRegFragmentArgs by navArgs()


    private fun checkStatusAndAction() {
        val biometricManager = BiometricManager.from(requireContext())

        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                waitingForResult = false
                saveBiometricEnabled()

                binding.bioScanUncheckIv.visibility = View.GONE
                binding.bioScanSuccessIv.visibility = View.VISIBLE
                binding.bioScanDetailTv.visibility = View.GONE
                binding.bioScanIntroTv.text = getText(R.string.bio_reg_success)
                binding.bottomBtnLayout.visibility = View.GONE // 버튼 숨기기

//                when (args.bioRegType) {
//                    "JOIN" -> {// 가이드 화면으로 이동
//                        handler.postDelayed({
//                            findNavController().navigate(R.id.action_bioRegFragment_to_pinFragment)
//                        }, 2000)
//                    }
//                    "LOGIN" -> {
//                        handler.postDelayed({
//                            findNavController().navigate(R.id.action_bioRegFragment_to_pinFragment)
//                        }, 2000)
//                    }
//                }


            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                waitingForResult = true
                moveToSettings()
            }


            else -> {
                waitingForResult = false
                binding.bioScanUncheckIv.visibility = View.GONE
                binding.bioScanErrorIv.visibility = View.VISIBLE
                binding.bioScanIntroTv.text = getText(R.string.bio_reg_failed)
                binding.bioScanDetailTv.visibility = View.GONE

                waitingForResult = true
                moveToSettings()

                setButtonText("재등록")
            }
        }
    }

    private fun saveBiometricEnabled() {
        val sharedPref = requireActivity().getSharedPreferences("BioPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("is_bio_login_enabled", true)
            apply()
        }
    }

    private fun setButtonText(text: String) {
        try {
            val textView = binding.loginPinInputActiveCv.getChildAt(0) as? TextView
            textView?.text = text
        } catch (e: Exception) {
        }
    }

    private fun moveToSettings() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val intent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG)
                }
                startActivity(intent)
            } else {
                throw Exception("Under Android 11")
            }
        } catch (e: Exception) {
            try {
                startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
            } catch (e2: Exception) {
                try {
                    startActivity(Intent(Settings.ACTION_SETTINGS))
                } catch (e3: Exception) {
                    Toast.makeText(requireContext(), "설정 화면으로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}