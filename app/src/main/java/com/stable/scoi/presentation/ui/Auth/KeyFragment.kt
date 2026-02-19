package com.stable.scoi.presentation.ui.Auth

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
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
        setupFocusListeners()
    }

    override fun initStates() {
        super.initStates()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvent.collect { event -> handleJoinEvent(event) }
                }
            }
        }
    }

    private fun handleJoinEvent(event: JoinEvent) {
        when(event) {
            is JoinEvent.NavigateToRegDone -> findNavController().navigate(R.id.action_KeyFragment_to_joinCompleteFragment)
            is JoinEvent.ShowError -> Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            else -> {}
        }
    }

    private fun setupListeners() {
        binding.apiKeyBackIv.setOnClickListener { findNavController().popBackStack() }

        binding.apiKeyExchangeLabelTv.setOnClickListener {
            ExchangeBottomSheet().show(childFragmentManager, "ExchangeBottomSheet")
        }

        childFragmentManager.setFragmentResultListener("exchangeKey", viewLifecycleOwner) { _, bundle ->
            bundle.getString("selectedExchange")?.let {
                binding.apiKeyExchangeLabelTv.text = it
                binding.apiKeyExchangeLabelTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_black))
                isExchangeSelected = true
                checkInputValidation()
            }
        }

        binding.apiKeyInputIv.setOnClickListener {
            binding.apiKeyInputEt.text?.clear()
        }
        binding.secretKeyInputIv.setOnClickListener {
            binding.secretKeyInputEt.text?.clear()
        }

        binding.apiKeyNextBtn.setOnClickListener {
            val exchangeType = when (binding.apiKeyExchangeLabelTv.text.toString()) {
                "빗썸" -> "BITHUMB"
                "업비트" -> "UPBIT"
                else -> ""
            }
            viewModel.submitSignUp(exchangeType, binding.apiKeyInputEt.text.toString(), binding.secretKeyInputEt.text.toString())
        }
    }

    private fun setupFocusListeners() {
        val focusPairs = listOf(
            binding.apiKeyInputEt to binding.apiKeyInputIv,
            binding.secretKeyInputEt to binding.secretKeyInputIv
        )

        focusPairs.forEach { (editText, clearIv) ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    editText.backgroundTintList=null
                    editText.setBackgroundResource(R.drawable.bg_underlin_black)
                    clearIv.visibility = if (editText.text?.isNotEmpty() == true) View.VISIBLE else View.GONE
                } else {
                    editText.setBackgroundResource(R.drawable.bg_pin_underline)
                    clearIv.visibility = View.GONE
                }
            }
        }
    }

    private fun setupTextWatchers() {
        binding.apiKeyInputEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.apiKeyInputIv.visibility = if (s?.isNotEmpty() == true && binding.apiKeyInputEt.hasFocus()) View.VISIBLE else View.GONE
                checkInputValidation()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.secretKeyInputEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.secretKeyInputIv.visibility = if (s?.isNotEmpty() == true && binding.secretKeyInputEt.hasFocus()) View.VISIBLE else View.GONE
                checkInputValidation()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun checkInputValidation() {
        val isValid = isExchangeSelected &&
                binding.apiKeyInputEt.text.toString().isNotBlank() &&
                binding.secretKeyInputEt.text.toString().isNotBlank()
        binding.apiKeyNextBtn.isEnabled = isValid
    }
}