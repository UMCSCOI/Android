package com.stable.scoi.presentation.data

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.stable.scoi.R

class PasswordFragment : Fragment(R.layout.fragment_password) {

    private var failCount = 0
    private val maxFailCount = 4

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.Transfer_backArrow_IV).setOnClickListener {
            findNavController().popBackStack()
        }

        val et1 = view.findViewById<EditText>(R.id.login_pin_1_et)
        val et2 = view.findViewById<EditText>(R.id.login_pin_2_et)
        val et3 = view.findViewById<EditText>(R.id.login_pin_3_et)
        val et4 = view.findViewById<EditText>(R.id.login_pin_4_et)
        val et5 = view.findViewById<EditText>(R.id.login_pin_5_et)
        val et6 = view.findViewById<EditText>(R.id.login_pin_6_et)

        val btnInput = view.findViewById<MaterialButton>(R.id.Transfer_password_input_TV)
        val tvError = view.findViewById<TextView>(R.id.tv_password_error)

        val tvForgot = view.findViewById<View>(R.id.Transfer_password_forgot_container)

        val editTexts = listOf(et1, et2, et3, et4, et5, et6)
        val errorColor = Color.RED
        val activeColor = Color.BLACK

        btnInput?.isEnabled = false
        btnInput?.alpha = 0.5f
        btnInput?.visibility = View.GONE

        tvForgot.setOnClickListener {
            val dialog = CustomDialog(
                title = "간편 비밀번호를 잊으셨나요?",
                content = "휴대폰 인증 후 새로운 비밀번호를\n설정할 수 있어요.\n먼저 휴대폰 인증부터 진행할게요.",
                iconResId = R.drawable.ic_forgot_password,
                onConfirm = {
                    findNavController().navigate(R.id.phoneAuthFragment)
                }
            )
            dialog.show(parentFragmentManager, "ForgotDialog")
        }

        editTexts.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && index < editTexts.size - 1) {
                        editTexts[index + 1].requestFocus()
                    }
                    if (tvError?.visibility == View.VISIBLE && s?.isNotEmpty() == true) {
                        tvError.visibility = View.GONE
                        editTexts.forEach { it.backgroundTintList = ColorStateList.valueOf(activeColor) }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    val isAllFilled = editTexts.all { it.text.length == 1 }
                    if (isAllFilled) {
                        btnInput?.isEnabled = true
                        btnInput?.alpha = 1.0f
                        btnInput?.visibility = View.VISIBLE

                        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(editTexts.last().windowToken, 0)
                        editTexts.last().clearFocus()
                    } else {
                        btnInput?.isEnabled = false
                        btnInput?.alpha = 0.5f
                        btnInput?.visibility = View.GONE
                    }
                }
            })
        }

        et6.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus()
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                true
            } else {
                false
            }
        }

        btnInput?.setOnClickListener {
            val inputPassword = editTexts.joinToString("") { it.text.toString() }
            val currentPassword = "111111" // 테스트용 정답

            if (inputPassword == currentPassword) {
                findNavController().popBackStack()
            } else {
                failCount++

                if (failCount >= maxFailCount) {
                    val dialog = CustomDialog(
                        title = "간편 비밀번호를\n다시 설정해 주세요",
                        content = "잘못된 비밀번호를 5번 넘게 입력했어요.\n계정 보호를 위해 본인 확인 후\n간편 비밀번호 재설정을 진행할게요.",
                        iconResId = R.drawable.ic_reset_password,
                        onConfirm = {
                            findNavController().navigate(R.id.phoneAuthFragment)
                        }
                    )
                    dialog.isCancelable = false
                    dialog.show(parentFragmentManager, "FailDialog")

                    tvError?.visibility = View.INVISIBLE
                } else {
                    tvError?.visibility = View.VISIBLE
                    tvError?.text = "잘못된 비밀번호입니다.\n5회 연속 실패 시 본인 인증 후 재설정이 필요합니다.\n($failCount/$maxFailCount)"

                    editTexts.forEach {
                        it.text.clear()
                        it.backgroundTintList = ColorStateList.valueOf(errorColor)
                    }
                    et1.requestFocus()

                    btnInput.isEnabled = false
                    btnInput.alpha = 0.5f
                    btnInput.visibility = View.GONE
                }
            }
        }
    }
}