package com.stable.scoi.presentation.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R

class ApiSettingFragment : Fragment(R.layout.fragment_api_setting) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }

        view.findViewById<View>(R.id.btn_change_mode).setOnClickListener {
            findNavController().navigate(R.id.action_apiSetting_to_apiEdit)
        }

        setFragmentResultListener("requestKey") { key, bundle ->
            val isUpdated = bundle.getBoolean("isUpdated")
            if (isUpdated) {
                // 실제로 바뀐 데이터 불러오는 코드 적어야함
                // 바뀐 데이터 ui에 갱신하는 코드 적기!!
            }
        }
    }
}