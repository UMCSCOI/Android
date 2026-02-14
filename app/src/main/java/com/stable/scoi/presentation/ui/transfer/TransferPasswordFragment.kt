package com.stable.scoi.presentation.ui.transfer

import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.text.method.TransformationMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentTransferPasswordBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.base.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransferPasswordFragment: BaseFragment<FragmentTransferPasswordBinding, TransferState, TransferEvent, TransferViewModel>(
    FragmentTransferPasswordBinding::inflate
) {
    lateinit var passwordFirst: String
    lateinit var passwordSecond: String
    lateinit var passwordThird: String
    lateinit var passwordFourth: String
    lateinit var passwordFifth: String
    lateinit var passwordSixth: String

    override val viewModel: TransferViewModel by activityViewModels()

    override fun initView() {

        binding.TransferPasswordInputTV.isEnabled = false

        //비밀번호 텍스트 변경 (암호화)
        binding.apply {
            TransferPasswordInputPassword1ET.applyBigDotMask()
            TransferPasswordInputPassword2ET.applyBigDotMask()
            TransferPasswordInputPassword3ET.applyBigDotMask()
            TransferPasswordInputPassword4ET.applyBigDotMask()
            TransferPasswordInputPassword5ET.applyBigDotMask()
            TransferPasswordInputPassword6ET.applyBigDotMask()
        }

        //EditText focus 자동 이동
        moveNext(
            binding.TransferPasswordInputPassword1ET,
            binding.TransferPasswordInputPassword2ET) { password ->
            passwordFirst = password
        }

        moveNext(
            binding.TransferPasswordInputPassword2ET,
            binding.TransferPasswordInputPassword3ET) { password ->
            passwordSecond = password
        }

        moveNext(
            binding.TransferPasswordInputPassword3ET,
            binding.TransferPasswordInputPassword4ET) { password ->
            passwordThird = password
        }

        moveNext(
            binding.TransferPasswordInputPassword4ET,
            binding.TransferPasswordInputPassword5ET) { password ->
            passwordFourth = password
        }

        moveNext(
            binding.TransferPasswordInputPassword5ET,
            binding.TransferPasswordInputPassword6ET) { password ->
            passwordFifth = password
        }

        moveNext(
            binding.TransferPasswordInputPassword6ET,
            binding.focusDummy) { password ->
            passwordSixth = password
        }

        //비밀번호 전송 + 출금
        binding.TransferPasswordInputTV.setOnClickListener {
            viewModel.submitPassword(passwordFirst, passwordSecond, passwordThird, passwordFourth, passwordFifth, passwordSixth)
            Log.d("password", viewModel.execute.value.simplePassword)
        }

        //이전 화면을 돌아가기
        binding.TransferBackArrowIV.setOnClickListener {
            findNavController().popBackStack()
        }

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiEvent.collect { event ->
                    when (event) {
                        TransferEvent.NavigateToNextPage -> findNavController().navigate(R.id.transfer_complete_fragment)
                        is TransferEvent.ShowError -> {
                            //경고 문구 표시
                            binding.apply {
                                resetEditText(TransferPasswordInputPassword1ET)
                                resetEditText(TransferPasswordInputPassword2ET)
                                resetEditText(TransferPasswordInputPassword3ET)
                                resetEditText(TransferPasswordInputPassword4ET)
                                resetEditText(TransferPasswordInputPassword5ET)
                                resetEditText(TransferPasswordInputPassword6ET)
                            }
                        }
                    }
                }
            }
        }
    }

    fun moveNext(
        editText: EditText,
        requestText: EditText,
        onPasswordEntered: (String) -> Unit)
    {
        editText.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(p0: Editable?)
            {
                if (!p0.isNullOrEmpty())
                {
                    editText.transformationMethod = PasswordTransformationMethod.getInstance()
                    onPasswordEntered(p0.toString())
                    editText.clearFocus()
                    editText.isFocusable = false
                    requestText.requestFocus()

                    if (requestText == binding.focusDummy) {
                        Log.d("action", "action")
                        binding.focusDummy.hideKeyboard()
                        binding.TransferPasswordInputTV.isEnabled = true
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    fun resetEditText(
        editText: EditText
    ) {
        editText.setText("")
        editText.isFocusable = true
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun EditText.applyBigDotMask() {
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