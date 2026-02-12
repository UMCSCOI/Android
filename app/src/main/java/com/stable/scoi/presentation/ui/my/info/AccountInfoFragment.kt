package com.stable.scoi.presentation.ui.my.info

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.databinding.FragmentAccountInfoBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.my.MyPageEvent
import com.stable.scoi.presentation.ui.my.MyPageUiState
import com.stable.scoi.presentation.ui.my.MyPageViewModel
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