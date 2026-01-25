package com.stable.scoi.presentation.base

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.stable.scoi.R

class AccountInfoFragment : Fragment(R.layout.fragment_account_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnIndividual = view.findViewById<MaterialButton>(R.id.btn_individual)
        val btnCorporate = view.findViewById<MaterialButton>(R.id.btn_corporate)
        val btnSave = view.findViewById<MaterialButton>(R.id.btn_save)
        val btnBack = view.findViewById<View>(R.id.back_iv)

        val colorBlue = Color.parseColor("#5C78FF")
        val colorGrayText = Color.parseColor("#888888")
        val colorGrayStroke = Color.parseColor("#DDDDDD")

        btnIndividual.setOnClickListener {
            btnIndividual.strokeColor = ColorStateList.valueOf(colorBlue)
            btnIndividual.setTextColor(colorBlue)

            btnCorporate.strokeColor = ColorStateList.valueOf(colorGrayStroke)
            btnCorporate.setTextColor(colorGrayText)
        }

        btnCorporate.setOnClickListener {
            btnCorporate.strokeColor = ColorStateList.valueOf(colorBlue)
            btnCorporate.setTextColor(colorBlue)

            btnIndividual.strokeColor = ColorStateList.valueOf(colorGrayStroke)
            btnIndividual.setTextColor(colorGrayText)
        }

        btnSave.setOnClickListener {
            // 서버 저장 코드 넣기

            findNavController().popBackStack()
        }

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}