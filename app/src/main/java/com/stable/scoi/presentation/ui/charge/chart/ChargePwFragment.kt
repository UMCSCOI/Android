package com.stable.scoi.presentation.ui.charge.chart

import android.graphics.Rect
import android.os.Build
import android.text.method.TransformationMethod
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.databinding.FragmentChargePwBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.util.SLOG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class ChargePwFragment :
    BaseFragment<FragmentChargePwBinding, ChargeUiState, ChargeEvent, ChargeViewModel>(
        FragmentChargePwBinding::inflate,
    ) {
    override val viewModel: ChargeViewModel by activityViewModels()

    private fun hideKeyboard() {
        val window = requireActivity().window
        WindowInsetsControllerCompat(window, binding.root).hide(WindowInsetsCompat.Type.ime())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        val pinEditTexts = with(binding) {
            listOf(loginPinReg1Et, loginPinReg2Et, loginPinReg3Et, loginPinReg4Et, loginPinReg5Et, loginPinReg6Et)
        }

        // 비밀번호 텍스트 변경 (암호화 - ● 표시)
        pinEditTexts.forEach { it.applyBigDotMask() }

        pinEditTexts.forEachIndexed { index, editText ->
            editText.doOnTextChanged { text, _, _, _ ->
                // 텍스트가 입력되었을 때만 처리
                if (text?.length == 1) {
                    if (index < 5) {
                        pinEditTexts[index + 1].requestFocus()
                    } else {
                        hideKeyboard()
                    }
                }

                // 데이터 전달
                val currentPin = pinEditTexts.joinToString("") { it.text.toString() }
                viewModel.onPinChanged(currentPin)

                SLOG.D("입력된 핀: $currentPin (현재 인덱스: $index)")
            }

            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    // 현재 칸이 비어있으면 이전 칸으로 이동해서 지우기
                    if (editText.text.isEmpty() && index > 0) {
                        pinEditTexts[index - 1].apply {
                            requestFocus()
                            text = null
                        }
                        return@setOnKeyListener true
                    }
                }
                false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiEvent.collect {
                    when(it) {
                        ChargeEvent.Complete -> navigateToChargeComplete()
                        ChargeEvent.MoveToBack -> findNavController().popBackStack()
                        else -> {}
                    }
                }
            }

            launch {
                viewModel.candleEvents.collect { ev ->

                }
            }
        }
    }

    private fun navigateToChargeComplete() {
        val action = ChargePwFragmentDirections.actionChargePwComplete(coin = viewModel.uiState.value.currentMarket, viewModel.uiState.value.count)
        findNavController().navigate(action)
    }

    private fun EditText.applyBigDotMask() {
        this.transformationMethod = object : TransformationMethod {
            override fun getTransformation(
                source: CharSequence,
                view: View?
            ): CharSequence {
                return BigDotCharSequence(source)
            }

            override fun onFocusChanged(
                view: View?,
                sourceText: CharSequence?,
                focused: Boolean,
                direction: Int,
                previouslyFocusedRect: Rect?
            ) {}
        }
    }
}

private class BigDotCharSequence(private val source: CharSequence) : CharSequence {
    override val length: Int
        get() = source.length

    override fun get(index: Int): Char {
        return '●'
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return BigDotCharSequence(source.subSequence(startIndex, endIndex))
    }
}