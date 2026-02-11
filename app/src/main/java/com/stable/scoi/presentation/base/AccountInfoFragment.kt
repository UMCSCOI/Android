package com.stable.scoi.presentation.base

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.databinding.FragmentAccountInfoBinding
import com.stable.scoi.presentation.mypage.MyPageEvent
import com.stable.scoi.presentation.mypage.MyPageUiState
import com.stable.scoi.presentation.mypage.MyPageViewModel
import com.stable.scoi.presentation.data.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountInfoFragment : BaseFragment<FragmentAccountInfoBinding, MyPageUiState, MyPageEvent, MyPageViewModel>(
    FragmentAccountInfoBinding::inflate
) {
    override val viewModel: MyPageViewModel by viewModels()

    override fun initView() {
        viewModel.loadUserInfo()

        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSave.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun initStates() {
        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.collectLatest { state ->
                    state.userInfo?.let { user ->

                        binding.tvName.text = user.koreanName

                        binding.etFirstName.text = user.englishName
                        binding.etLastName.text = ""

                        binding.tvMaskedId.text = user.residentNumber
                    }
                }
            }
        }
    }
}