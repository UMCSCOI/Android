package com.stable.scoi.presentation.ui.login

import android.view.KeyEvent
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentLoginPinRegBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.Auth.JoinEvent
import com.stable.scoi.presentation.ui.Auth.JoinState
import com.stable.scoi.presentation.ui.Auth.JoinViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginRegFragment : BaseFragment<FragmentLoginPinRegBinding, LoginState, LoginEvent, LoginViewModel>(
    FragmentLoginPinRegBinding::inflate
) {
    override val viewModel: LoginViewModel by activityViewModels()
    private val joinViewModel: JoinViewModel by activityViewModels()

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
                joinViewModel.onPinChanged(currentPin)
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
                joinViewModel.uiState.collect { state ->
                    renderJoinUi(state)
                }
            }

            launch {
                joinViewModel.uiEvent.collect { event ->
                    handleJoinEvent(event)
                }
            }
        }
    }

    private fun renderJoinUi(state: JoinState) {
        if (state.isButtonEnabled) {
            binding.loginPinRegInputActiveCv.visibility = View.VISIBLE
            binding.loginPinInputInactiveCv.visibility = View.GONE
        } else {
            binding.loginPinRegInputActiveCv.visibility = View.GONE
            binding.loginPinInputInactiveCv.visibility = View.VISIBLE
        }
    }

    private fun handleJoinEvent(event: JoinEvent) {
        when (event) {
            is JoinEvent.NavigateToRegDone -> {
                findNavController().navigate(R.id.action_loginRegFragment_to_loginRegDoneFragment)
            }
            is JoinEvent.ShowError -> {
            }
            else -> {}
        }
    }
    override fun onResume() {
        super.onResume()
        binding.loginPinReg1Et.postDelayed({
            val pinEditTexts = listOf(
                binding.loginPinReg1Et, binding.loginPinReg2Et, binding.loginPinReg3Et,
                binding.loginPinReg4Et, binding.loginPinReg5Et, binding.loginPinReg6Et
            )
            val targetEt = pinEditTexts.firstOrNull { it.text.isNullOrEmpty() } ?: pinEditTexts.last()
            binding.root.clearFocus()
            targetEt.requestFocus()
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