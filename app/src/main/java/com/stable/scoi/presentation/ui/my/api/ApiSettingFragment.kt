package com.stable.scoi.presentation.ui.my.api

import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentApiSettingBinding
import com.stable.scoi.presentation.base.BaseFragment
import com.stable.scoi.presentation.ui.my.MyPageViewModel
import com.stable.scoi.presentation.ui.my.info.MyPageEvent
import com.stable.scoi.presentation.ui.my.info.MyPageUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ApiSettingFragment : BaseFragment<FragmentApiSettingBinding, MyPageUiState, MyPageEvent, MyPageViewModel>(
    FragmentApiSettingBinding::inflate
) {
    override val viewModel: MyPageViewModel by viewModels()

    override fun initView() {
        // API 설정 정보 로드
        viewModel.loadApiSettings()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnChangeMode.setOnClickListener {
            findNavController().navigate(R.id.action_apiSetting_to_apiEdit)
        }

        // ApiEditFragment 등에서 수정 후 돌아왔을 때 데이터 갱신
        setFragmentResultListener("requestKey") { _, bundle ->
            val isUpdated = bundle.getBoolean("isUpdated")
            if (isUpdated) {
                viewModel.loadApiSettings() // 데이터 다시 불러오기
            }
        }
    }

    override fun initStates() {
        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.collect { state ->
                    // apiKeys 리스트를 순회하며 UI 업데이트
                    val keys = state.apiKeys

                    // 초기화 (데이터가 없을 경우를 대비해 빈 값으로 설정)
                    binding.tvBithumbPublic.text = "등록된 키 없음"
                    binding.tvBithumbSecret.text = "등록된 키 없음"
                    binding.tvUpbitPublic.text = "등록된 키 없음"
                    binding.tvUpbitSecret.text = "등록된 키 없음"

                    keys.forEach { keyInfo ->
                        when (keyInfo.exchangeType) { // exchange 필드명은 데이터 모델에 따라 다를 수 있음 (예: exchangeName)
                            "BITHUMB" -> {
                                binding.tvBithumbPublic.text = keyInfo.publicKey
                                // 시크릿 키는 보통 마스킹하거나 숨김 처리하지만, 요구사항대로 표시
                                binding.tvBithumbSecret.text = keyInfo.secretKey
                            }
                            "UPBIT" -> {
                                binding.tvUpbitPublic.text = keyInfo.publicKey
                                binding.tvUpbitSecret.text = keyInfo.secretKey
                            }
                        }
                    }
                }
            }
        }
    }
}