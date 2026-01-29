package com.stable.scoi.presentation.base

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
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


        binding.TransferPasswordInputTV.setOnClickListener {
            viewModel.submitPassword(passwordFirst, passwordSecond, passwordThird, passwordFourth, passwordFifth, passwordSixth)
            Log.d("password", viewModel.execute.value.simplePassword)
            findNavController().navigate(R.id.transfer_complete_fragment)
        }
    }

    fun moveNext(
        editText: EditText,
        requestText: EditText,
        onPasswordEntered: (String) -> Unit)
    {
        editText.addTextChangedListener(object: TextWatcher{

            override fun afterTextChanged(p0: Editable?)
            {
                if (!p0.isNullOrEmpty())
                {
                    editText.transformationMethod = PasswordTransformationMethod.getInstance()
                    onPasswordEntered(p0.toString())
                    editText.clearFocus()
                    requestText.requestFocus()

                    if (requestText == binding.focusDummy) {
                        Log.d("action", "action")
                        binding.focusDummy.hideKeyboard()
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}