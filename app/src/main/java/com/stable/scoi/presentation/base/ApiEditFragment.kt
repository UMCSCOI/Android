package com.stable.scoi.presentation

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.stable.scoi.R

class ApiEditFragment : Fragment(R.layout.fragment_api_edit) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.btnBack_edit).setOnClickListener {
            findNavController().popBackStack()
        }

        val etBithumbPublic = view.findViewById<EditText>(R.id.et_bithumb_public)
        val etBithumbSecret = view.findViewById<EditText>(R.id.et_bithumb_secret)

        val etUpbitPublic = view.findViewById<EditText>(R.id.et_upbit_public)
        val etUpbitSecret = view.findViewById<EditText>(R.id.et_upbit_secret)

        view.findViewById<MaterialButton>(R.id.btn_save_changes).setOnClickListener {

            val bithumbPublic = etBithumbPublic?.text.toString() ?: ""
            val bithumbSecret = etBithumbSecret?.text.toString() ?: ""

            val upbitPublic = etUpbitPublic?.text.toString() ?: ""
            val upbitSecret = etUpbitSecret?.text.toString() ?: ""

            val result = bundleOf(
                "isUpdated" to true,
                "bithumbPublicKey" to bithumbPublic,
                "bithumbSecretKey" to bithumbSecret,
                "upbitPublicKey" to upbitPublic,
                "upbitSecretKey" to upbitSecret
            )

            setFragmentResult("requestKey", result)

            findNavController().popBackStack()
        }
    }
}