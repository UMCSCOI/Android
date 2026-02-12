package com.stable.scoi.presentation.ui.login

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentLoginPinDoneBinding
import com.stable.scoi.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class LoginRegDoneFragment : BaseFragment<FragmentLoginPinDoneBinding, LoginState, LoginEvent, LoginViewModel>(
    FragmentLoginPinDoneBinding::inflate
) {
    private val handler = Handler(Looper.getMainLooper())
    override val viewModel: LoginViewModel by activityViewModels()
    override fun initView() {
        handler.postDelayed({
            findNavController().navigate(R.id.action_loginRegDoneFragment_to_bioRegFragment)
        }, 2000)
    }
}