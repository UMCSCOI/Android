package com.stable.scoi.presentation

import android.os.Bundle
import android.view.View
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

        view.findViewById<MaterialButton>(R.id.btn_save_changes).setOnClickListener {
            // EditText에 입력된 내용 저장하는 로직 적기!!

            setFragmentResult("requestKey", bundleOf("isUpdated" to true))

            findNavController().popBackStack()
        }
    }
}