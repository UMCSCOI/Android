package com.stable.scoi.presentation.ui.my.changePw

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

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    private var step = 1
    private var firstPassword = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<View>(R.id.Transfer_backArrow_IV)

        val tvTitlePrefix = view.findViewById<TextView>(R.id.Trasnfer_password_TV)
        val tvSimplePw = view.findViewById<TextView>(R.id.simple_password) // "간편 비밀번호"
        val tvSuffix = view.findViewById<TextView>(R.id.tv_input_please) // "를 입력해주세요" (XML ID 추가 필요, 아래 설명 참고)

        // 하단 설명 텍스트들 (2단계에서 숨기기 위해)
        val tvDesc1 = view.findViewById<TextView>(R.id.now_password)
        val tvDesc2 = view.findViewById<TextView>(R.id.use_password)
        val tvDesc3 = view.findViewById<TextView>(R.id.desc_suffix) // " 사용할 비밀번호예요." (XML ID 추가 필요)

        val et1 = view.findViewById<EditText>(R.id.login_pin_1_et)
        val et2 = view.findViewById<EditText>(R.id.login_pin_2_et)
        val et3 = view.findViewById<EditText>(R.id.login_pin_3_et)
        val et4 = view.findViewById<EditText>(R.id.login_pin_4_et)
        val et5 = view.findViewById<EditText>(R.id.login_pin_5_et)
        val et6 = view.findViewById<EditText>(R.id.login_pin_6_et)

        val btnInput = view.findViewById<MaterialButton>(R.id.Transfer_password_input_TV)
        val tvError = view.findViewById<TextView>(R.id.tv_password_error)

        view.findViewById<View>(R.id.Transfer_password_forgot_container).visibility = View.GONE

        val editTexts = listOf(et1, et2, et3, et4, et5, et6)
        val errorColor = Color.RED
        val activeColor = Color.BLACK

        btnInput?.isEnabled = false
        btnInput?.alpha = 0.5f
        btnInput?.visibility = View.GONE

        btnBack.setOnClickListener {
            if (step == 2) {
                resetToStep1(editTexts, tvTitlePrefix, tvSimplePw, tvSuffix, tvDesc1, tvDesc2, tvDesc3, tvError, btnInput)
            } else {
                findNavController().popBackStack()
            }
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
            val currentInput = editTexts.joinToString("") { it.text.toString() }

            if (step == 1) {
                firstPassword = currentInput
                step = 2

                updateUiForStep2(tvTitlePrefix, tvSimplePw, tvSuffix, tvDesc1, tvDesc2, tvDesc3)
                editTexts.forEach { it.text.clear() }
                et1.requestFocus()

                btnInput.isEnabled = false
                btnInput.alpha = 0.5f
                btnInput.visibility = View.GONE

                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(et1, InputMethodManager.SHOW_IMPLICIT)

            } else {
                if (currentInput == firstPassword) {
                    findNavController().navigate(R.id.passwordSuccessFragment)
                } else {
                    tvError?.visibility = View.VISIBLE
                    tvError?.text = "비밀번호가 일치하지 않습니다.\n다시 입력해주세요."

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

    private fun updateUiForStep2(
        tvPrefix: TextView, tvSimple: TextView, tvSuffix: TextView?,
        d1: TextView, d2: TextView, d3: TextView?
    ) {
        tvPrefix.text = "확인을 위해 변경할\n간편 비밀번호를 재입력해주세요."

        tvSimple.visibility = View.GONE
        tvSuffix?.visibility = View.GONE

        d1.visibility = View.GONE
        d2.visibility = View.GONE
        d3?.visibility = View.GONE
    }

    private fun resetToStep1(
        editTexts: List<EditText>,
        tvPrefix: TextView, tvSimple: TextView, tvSuffix: TextView?,
        d1: TextView, d2: TextView, d3: TextView?,
        tvError: TextView?, btnInput: MaterialButton?
    ) {
        step = 1
        firstPassword = ""

        tvPrefix.text = "변경할"
        tvSimple.visibility = View.VISIBLE
        tvSuffix?.visibility = View.VISIBLE
        d1.visibility = View.VISIBLE
        d2.visibility = View.VISIBLE
        d3?.visibility = View.VISIBLE

        tvError?.visibility = View.GONE

        editTexts.forEach { it.text.clear() }
        editTexts[0].requestFocus()

        btnInput?.isEnabled = false
        btnInput?.alpha = 0.5f
        btnInput?.visibility = View.GONE
    }
}