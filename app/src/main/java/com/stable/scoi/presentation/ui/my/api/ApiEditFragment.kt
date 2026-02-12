package com.stable.scoi.presentation.ui.my.api

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.databinding.FragmentApiEditBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.my.MyPageViewModel
import com.stable.scoi.presentation.ui.my.info.MyPageEvent
import com.stable.scoi.presentation.ui.my.info.MyPageUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ApiEditFragment :
    BaseFragment<FragmentApiEditBinding, MyPageUiState, MyPageEvent, MyPageViewModel>(
        FragmentApiEditBinding::inflate
    ) {
    override val viewModel: MyPageViewModel by viewModels()

    override fun initView() {
        viewModel.loadApiSettings()

        binding.apply {
            btnBackEdit.setOnClickListener {
                findNavController().popBackStack()
            }

            btnSaveChanges.setOnClickListener {
                val bithumbPublic = etBithumbPublic.text.toString().trim()
                val bithumbSecret = etBithumbSecret.text.toString().trim()
                val upbitPublic = etUpbitPublic.text.toString().trim()
                val upbitSecret = etUpbitSecret.text.toString().trim()

                // ViewModel의 저장 함수 호출
                viewModel.saveApiKeys(
                    upbitPublic = upbitPublic,
                    upbitSecret = upbitSecret,
                    bithumbPublic = bithumbPublic,
                    bithumbSecret = bithumbSecret
                )
            }
        }
    }

    override fun initStates() {
        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiEvent.collect { event ->
                    when (event) {
                        is MyPageEvent.SaveSuccess -> {
                            // 이전 화면(ApiSettingFragment)에 갱신 신호 전달
                            setFragmentResult("requestKey", bundleOf("isUpdated" to true))
                            findNavController().popBackStack()
                        }

                        is MyPageEvent.ShowToast -> {
                            //showToast(event.message)
                        }

                        else -> {}
                    }
                }
            }

            launch {
                viewModel.uiState.collectLatest { state ->
                    if (!state.isLoading && binding.etBithumbPublic.text.isEmpty()) {
                        state.apiKeys.forEach { key ->
                            when (key.exchangeType) {
                                "BITHUMB" -> {
                                    binding.etBithumbPublic.setText(key.publicKey)
                                    binding.etBithumbSecret.setText(key.secretKey)
                                }

                                "UPBIT" -> {
                                    binding.etUpbitPublic.setText(key.publicKey)
                                    binding.etUpbitSecret.setText(key.secretKey)
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}