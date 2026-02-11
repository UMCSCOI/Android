package com.stable.scoi.presentation.data

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.stable.scoi.R

class PasswordSuccessFragment : Fragment(R.layout.fragment_password_success) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.btn_confirm).setOnClickListener {
            findNavController().popBackStack()
        }
    }
}