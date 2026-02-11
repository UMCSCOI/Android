package com.stable.scoi.presentation.data

import android.os.Bundle
import android.view.View
import android.widget.TextView
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
                val bithumbPublic = bundle.getString("bithumbPublicKey")
                val bithumbSecret = bundle.getString("bithumbSecretKey")

                if (!bithumbPublic.isNullOrEmpty()) {
                    view.findViewById<TextView>(R.id.tv_bithumb_public)?.text = bithumbPublic
                }
                if (!bithumbSecret.isNullOrEmpty()) {
                    view.findViewById<TextView>(R.id.tv_bithumb_secret)?.text = bithumbSecret
                }

                val upbitPublic = bundle.getString("upbitPublicKey")
                val upbitSecret = bundle.getString("upbitSecretKey")

                if (!upbitPublic.isNullOrEmpty()) {
                    view.findViewById<TextView>(R.id.tv_upbit_public)?.text = upbitPublic
                }
                if (!upbitSecret.isNullOrEmpty()) {
                    view.findViewById<TextView>(R.id.tv_upbit_secret)?.text = upbitSecret
                }
            }
        }
    }
}