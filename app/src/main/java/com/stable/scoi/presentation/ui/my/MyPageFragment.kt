package com.stable.scoi.presentation.ui.my

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentMypageBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.my.info.MyPageEvent
import com.stable.scoi.presentation.ui.my.info.MyPageUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding, MyPageUiState, MyPageEvent, MyPageViewModel>(
    FragmentMypageBinding::inflate
) {
    override val viewModel: MyPageViewModel by viewModels()

    override fun initView() {
        viewModel.loadUserInfo()

        binding.apply {
            menuAccountInfo.setOnClickListener {
                findNavController().navigate(R.id.action_mypage_to_accountInfo)
            }

            menuApiSetting.setOnClickListener {
                findNavController().navigate(R.id.action_mypage_to_apiSetting)
            }

            menuPasswordChange.setOnClickListener {
                findNavController().navigate(R.id.action_mypage_to_changePassword)
            }

        }
    }

    override fun initStates() {
        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.collectLatest { state ->
                    state.userInfo?.let { user ->
                        binding.tvUserName.text = user.koreanName
                    }
                }
            }
        }
    }
}