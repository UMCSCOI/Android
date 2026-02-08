package com.stable.scoi.presentation.ui.login

import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentLoginPinRegBinding
import com.stable.scoi.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginRegFragment : BaseFragment<FragmentLoginPinRegBinding, LoginState, LoginEvent, LoginViewModel>(
    FragmentLoginPinRegBinding::inflate
) {
    override val viewModel: LoginViewModel by activityViewModels()

    override fun onPause() {
        super.onPause()

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        val bottomNav = requireActivity().findViewById<View>(R.id.layout_bottom_nav)
        bottomNav?.visibility = View.VISIBLE
    }

    override fun initView() {

        val pinEditTexts = listOf(
            binding.loginPinReg1Et, binding.loginPinReg2Et, binding.loginPinReg3Et,
            binding.loginPinReg4Et, binding.loginPinReg5Et, binding.loginPinReg6Et
        )

        pinEditTexts.forEachIndexed { index, editText ->
            editText.doOnTextChanged { text, _, _, _ ->
                if (text?.length == 1) {
                    if (index < 5) {
                        pinEditTexts[index + 1].requestFocus()
                    } else {
                        hideKeyboard()
                    }
                }

                val currentPin = pinEditTexts.joinToString("") { it.text.toString() }
                viewModel.onPinChanged(currentPin)

            }

            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (editText.text.isEmpty() && index > 0) {
                        val prevEt = pinEditTexts[index - 1]
                        prevEt.requestFocus()
                        prevEt.text = null
                        return@setOnKeyListener true
                    }
                }
                false
            }
        }


        binding.loginPinRegInputActiveCv.setOnClickListener {
            findNavController().navigate(R.id.action_loginRegFragment_to_loginRegDoneFragment)
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.collect { state ->
                    renderUi(state)
                }
            }

            launch {
                viewModel.uiEvent.collect { event ->
                    handleEvent(event)
                }
            }
        }
    }


    private fun renderUi(state: LoginState) {
        if (state.isButtonEnabled) {
            binding.loginPinRegInputActiveCv.visibility = View.VISIBLE
            binding.loginPinInputInactiveCv.visibility = View.GONE
        } else {
            binding.loginPinRegInputActiveCv.visibility = View.GONE
            binding.loginPinInputInactiveCv.visibility = View.VISIBLE
        }
    }


    private fun handleEvent(event: LoginEvent) {
        val pinEditTexts = listOf(
            binding.loginPinReg1Et, binding.loginPinReg2Et, binding.loginPinReg3Et,
            binding.loginPinReg4Et, binding.loginPinReg5Et, binding.loginPinReg6Et
        )
        when (event) {
            is LoginEvent.NavigationToMain -> {

            }
            is LoginEvent.NavigationToBiometric->{

            }
            is LoginEvent.ShowError -> {

                }
            is LoginEvent.NavigationToExpired -> {

            }

            is LoginEvent.VerifySuccess -> {

            }
            is LoginEvent.NavigationToLogin -> {

            }
        }
    }

    override fun onResume() {
        super.onResume()

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val bottomNav = requireActivity().findViewById<View>(R.id.layout_bottom_nav)
        bottomNav?.visibility = View.GONE

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        binding.loginPinReg1Et.postDelayed({
            val pinEditTexts = listOf(
                binding.loginPinReg1Et, binding.loginPinReg2Et, binding.loginPinReg3Et,
                binding.loginPinReg4Et, binding.loginPinReg5Et, binding.loginPinReg6Et
            )

            val targetEt = pinEditTexts.firstOrNull { it.text.isNullOrEmpty() } ?: pinEditTexts.last()

            binding.root.clearFocus()

            targetEt.setSelection(targetEt.text?.length ?: 0)

            showKeyboard(targetEt)
        }, 300)
    }

    private fun showKeyboard(view: View) {
        if (view.requestFocus()) {
            val window = requireActivity().window
            WindowInsetsControllerCompat(window, view).show(WindowInsetsCompat.Type.ime())
        }
    }

    private fun hideKeyboard() {
        val window = requireActivity().window
        WindowInsetsControllerCompat(window, binding.root).hide(WindowInsetsCompat.Type.ime())
    }
}