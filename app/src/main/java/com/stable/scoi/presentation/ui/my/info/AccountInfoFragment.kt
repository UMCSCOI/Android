package com.stable.scoi.presentation.ui.my.info

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.databinding.FragmentAccountInfoBinding
import com.stable.scoi.presentation.base.BaseFragment
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

                        val engName = user.englishName ?: ""
                        val nameParts = engName.split(" ", limit = 2)

                        binding.etLastName.text = nameParts.getOrNull(0) ?: ""  // KIM
                        binding.etFirstName.text = nameParts.getOrNull(1) ?: "" // TEST

                        // 주민번호 마스킹 처리
                        binding.tvMaskedId.text = user.residentNumber?.let { rrn ->
                            if (rrn.length >= 8) {
                                "${rrn.substring(0, 8)}"
                            } else {
                                rrn
                            }
                        } ?: ""
                    }
                }
            }
        }
    }
}