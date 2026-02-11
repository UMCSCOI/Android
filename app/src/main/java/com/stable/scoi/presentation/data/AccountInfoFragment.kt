package com.stable.scoi.presentation.data

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.stable.scoi.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountInfoFragment : Fragment(R.layout.fragment_account_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSave = view.findViewById<MaterialButton>(R.id.btn_save)
        val btnBack = view.findViewById<View>(R.id.back_iv)

        btnSave.setOnClickListener {
            findNavController().popBackStack()
        }

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}