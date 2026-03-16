package com.stable.scoi.presentation.ui.Auth // 패키지명은 프로젝트에 맞게 확인하세요

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.data.local.PreferenceManager
import com.stable.scoi.databinding.FragmentRegCompleteBinding
import com.stable.scoi.presentation.MainActivity

class JoinCompleteFragment : Fragment() {
    private val preferenceManager: PreferenceManager by lazy { PreferenceManager(requireContext()) }
    private var _binding: FragmentRegCompleteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JoinViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        // 1. 뒤로가기 버튼
        binding.guideRegBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.guideActiveCv.setOnClickListener {
            navigateToPin()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun navigateToPin() {
        preferenceManager.setJoinStatus(true)
        findNavController().navigate(R.id.action_joinCompleteFragment_to_homeFragment)
    }
}

