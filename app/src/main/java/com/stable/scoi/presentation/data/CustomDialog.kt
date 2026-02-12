package com.stable.scoi.presentation.data

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.stable.scoi.R

class CustomDialog(
    private val title: String,
    private val content: String,
    private val buttonText: String = "휴대폰 인증",
    private val onConfirm: () -> Unit,
    private val iconResId: Int,
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_custom_popup, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvTitle = view.findViewById<TextView>(R.id.dialog_title_tv)
        val tvContent = view.findViewById<TextView>(R.id.dialog_content_tv)
        val tvBtn = view.findViewById<TextView>(R.id.dialog_btn_tv)

        val ivIcon = view.findViewById<ImageView>(R.id.dialog_icon_iv)
        ivIcon.setImageResource(iconResId)

        tvTitle.text = title
        tvContent.text = content
        tvBtn.text = buttonText

        tvBtn.setOnClickListener {
            onConfirm()
            dismiss()
        }

        return view
    }
}