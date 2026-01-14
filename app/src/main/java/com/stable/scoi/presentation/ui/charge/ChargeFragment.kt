package com.stable.scoi.presentation.ui.charge

import androidx.fragment.app.viewModels
import com.stable.scoi.databinding.FragmentChargeBinding
import com.stable.scoi.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChargeFragment : BaseFragment<FragmentChargeBinding, ChargeUiState, ChargeEvent, ChargeViewModel>(
    FragmentChargeBinding::inflate,
) {
    override val viewModel: ChargeViewModel by viewModels()

    override fun initView() {
        binding.apply {
            vm = viewModel
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiEvent.collect {

                }
            }

            launch {
                viewModel.uiState.collect {
                    // TODO 상태 관리
                }
            }
        }
    }
}