package com.stable.scoi.presentation.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R

class MyPageFragment : Fragment(R.layout.fragment_mypage) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.menu_account_info).setOnClickListener {
            findNavController().navigate(R.id.action_mypage_to_accountInfo)
        }

        view.findViewById<View>(R.id.menu_api_setting).setOnClickListener {
            findNavController().navigate(R.id.action_mypage_to_apiSetting)
        }

        view.findViewById<View>(R.id.menu_password_change).setOnClickListener {
            findNavController().navigate(R.id.action_mypage_to_changePassword)
        }
    }
}