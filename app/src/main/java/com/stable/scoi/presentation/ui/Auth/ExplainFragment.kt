package com.stable.scoi.presentation.ui.Auth

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentExplainBinding
import com.stable.scoi.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ExplainFragment: BaseFragment<FragmentExplainBinding,JoinState,JoinEvent,JoinViewModel>(
    FragmentExplainBinding::inflate
) {
    override val viewModel: JoinViewModel by activityViewModels()

    override fun initView() {
        binding.guideRegBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.guideActiveCv.setOnClickListener {
            findNavController().navigate(R.id.action_explainFragment_to_guideFragment)
        }
        binding.explainSkipTv.setOnClickListener {
            findNavController().navigate(R.id.action_explainFragment_to_keyFragment)
        }


    }

}