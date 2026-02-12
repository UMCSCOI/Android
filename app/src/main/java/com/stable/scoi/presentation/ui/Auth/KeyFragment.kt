package com.stable.scoi.presentation.ui.Auth

import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentKeyBinding
import com.stable.scoi.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KeyFragment : BaseFragment<FragmentKeyBinding, JoinState, JoinEvent, JoinViewModel>(
    FragmentKeyBinding::inflate
) {
    override val viewModel: JoinViewModel by activityViewModels()

    private var isExchangeSelected = false

    override fun initView() {
        binding.apiKeyNextBtn.isEnabled = false
        setupListeners()
        setupTextWatchers()
    }

    override fun initStates() {
        super.initStates()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvent.collect { event ->
                        handleJoinEvent(event)
                    }
                }
            }
        }
    }

    private fun handleJoinEvent(event: JoinEvent) {
        when(event) {
            is JoinEvent.NavigateToRegDone -> {
                findNavController().navigate(R.id.action_KeyFragment_to_joinCompleteFragment)
            }
            is JoinEvent.ShowError -> {
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            }
            is JoinEvent.NavigateToJoinComplete ->{

            }
            is JoinEvent.VerifySuccess -> {

            }
            is JoinEvent.NavigateToLogin -> {

            }
            is JoinEvent.OnSignUpClick -> {

            }
            is JoinEvent.OnInputChanged -> {

            }
            is JoinEvent.NavigateToPinRegister -> {

            }
        }
    }

    private fun setupListeners() {
        binding.apiKeyBackIv.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.apiKeyExchangeLabelTv.setOnClickListener {
            val bottomSheet = ExchangeBottomSheet()
            bottomSheet.show(childFragmentManager, "ExchangeBottomSheet")
        }

        childFragmentManager.setFragmentResultListener("exchangeKey", viewLifecycleOwner) { _, bundle ->
            val selectedExchange = bundle.getString("selectedExchange")

            selectedExchange?.let {
                binding.apiKeyExchangeLabelTv.text = it
                binding.apiKeyExchangeLabelTv.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.main_black)
                )

                isExchangeSelected = true
                checkInputValidation()
            }
        }

        binding.apiKeyNextBtn.setOnClickListener {
            val exchangeLabel = binding.apiKeyExchangeLabelTv.text.toString()

            val exchangeType = when (exchangeLabel) {
                "빗썸" -> "BITHUMB"
                "업비트" -> "UPBIT"
                else -> ""
            }
            val apiKey = binding.apiKeyInputEt.text.toString()
            val secretKey = binding.secretKeyInputEt.text.toString()

            viewModel.submitSignUp(exchangeType, apiKey, secretKey)
        }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputValidation()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.apiKeyInputEt.addTextChangedListener(textWatcher)
        binding.secretKeyInputEt.addTextChangedListener(textWatcher)
    }

    private fun checkInputValidation() {
        val isApiKeyFilled = binding.apiKeyInputEt.text.toString().isNotBlank()
        val isSecretKeyFilled = binding.secretKeyInputEt.text.toString().isNotBlank()

        val isValid = isExchangeSelected && isApiKeyFilled && isSecretKeyFilled

        binding.apiKeyNextBtn.isEnabled = isValid
    }
}