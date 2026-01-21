package com.stable.scoi.presentation.base

import android.content.Context
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.view.inspector.WindowInspector
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.stable.scoi.databinding.FragmentTransferBinding
import com.stable.scoi.databinding.FragmentTransferBinding.inflate
import com.stable.scoi.databinding.FragmentTransferPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

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

        fun View.hideKeyboard() {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }

        binding.TransferPasswordInputPassword1ET.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.TransferPasswordInputPassword1ET.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordFirst = binding.TransferPasswordInputPassword1ET.text.toString()
                binding.TransferPasswordInputPassword1ET.clearFocus()
                binding.TransferPasswordInputPassword2ET.requestFocus()
                true
            }
            else {
                false
            }
        }

        binding.TransferPasswordInputPassword2ET.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.TransferPasswordInputPassword2ET.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordSecond = binding.TransferPasswordInputPassword2ET.text.toString()
                binding.TransferPasswordInputPassword2ET.clearFocus()
                binding.TransferPasswordInputPassword3ET.requestFocus()
                true
            }
            else {
                false
            }
        }

        binding.TransferPasswordInputPassword3ET.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.TransferPasswordInputPassword3ET.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordThird = binding.TransferPasswordInputPassword3ET.text.toString()
                binding.TransferPasswordInputPassword3ET.clearFocus()
                binding.TransferPasswordInputPassword4ET.requestFocus()
                true
            }
            else {
                false
            }
        }

        binding.TransferPasswordInputPassword4ET.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.TransferPasswordInputPassword4ET.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordFourth = binding.TransferPasswordInputPassword4ET.text.toString()
                binding.TransferPasswordInputPassword4ET.clearFocus()
                binding.TransferPasswordInputPassword5ET.requestFocus()
                true
            }
            else {
                false
            }
        }

        binding.TransferPasswordInputPassword5ET.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.TransferPasswordInputPassword5ET.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordFifth = binding.TransferPasswordInputPassword5ET.text.toString()
                binding.TransferPasswordInputPassword5ET.clearFocus()
                binding.TransferPasswordInputPassword6ET.requestFocus()
                true
            }
            else {
                false
            }
        }

        binding.TransferPasswordInputPassword6ET.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.TransferPasswordInputPassword6ET.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordSixth = binding.TransferPasswordInputPassword6ET.text.toString()
                binding.TransferPasswordInputPassword6ET.clearFocus()
                binding.focusDummy.requestFocus()
                binding.TransferPasswordInputPassword6ET.hideKeyboard()
                true
            }
            else {
                false
            }
        }

        binding.TransferPasswordInputTV.setOnClickListener {
            viewModel.submitPassword(passwordFirst, passwordSecond, passwordThird, passwordFourth, passwordFifth, passwordSixth)
            Log.d("password", viewModel.execute.value.simplePassword)
        }
    }
}