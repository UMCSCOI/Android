package com.stable.scoi.presentation.ui.Auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentGuideStepBinding
import com.stable.scoi.presentation.ui.guide.model.GuideStep

class GuideFragment : Fragment() {

    private var _binding: FragmentGuideStepBinding? = null
    private val binding get() = _binding!!
    private val guideAdapter by lazy { GuideAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuideStepBinding.inflate(inflater, container, false)
        binding.apiKeyInputTv.setOnClickListener {
            findNavController().navigate(R.id.action_guideFragment_to_keyFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupIndicator()
        setupTabListeners()

        loadGuideData("BITHUMB")
        updateTabUI(isBithumb = true)
    }


    private fun setupViewPager() {
        binding.guideViewPager.adapter = guideAdapter
    }


    private fun setupIndicator() {
        com.google.android.material.tabs.TabLayoutMediator(binding.guideIndicator, binding.guideViewPager) { tab, position ->
        }.attach()
    }



    private fun setupTabListeners() {
        binding.bithumbTabCv.setOnClickListener {
            loadGuideData("BITHUMB")
            updateTabUI(isBithumb = true)
        }
        binding.upbitTabCv.setOnClickListener {
            loadGuideData("UPBIT")
            updateTabUI(isBithumb = false)
        }
    }

    private fun loadGuideData(type: String) {
        val data = if (type == "BITHUMB") {
            listOf(
                GuideStep("01", "빗썸 설정", "빗썸 앱에서 API를 활성화하세요.", R.drawable.bithumb_1),
                GuideStep("02", "키 복사", "발급된 키를 복사합니다.", R.drawable.bithumb_1) // 예시 데이터
            )
        } else {
            listOf(
                GuideStep("01", "업비트 설정", "업비트 고객센터에서 신청하세요.", R.drawable.bithumb_1) // 이미지 교체 필요
            )
        }

        guideAdapter.submitList(data)
        binding.guideViewPager.post { binding.guideViewPager.setCurrentItem(0, false) }
    }

    private fun updateTabUI(isBithumb: Boolean) {
        val activeColor = resources.getColor(R.color.active_fill, null)
        val inactiveColor = android.graphics.Color.TRANSPARENT
        val activeTextColor = resources.getColor(R.color.active, null)
        val inactiveTextColor = resources.getColor(R.color.disabled, null)

        if (isBithumb) {
            binding.bithumbTabCv.setCardBackgroundColor(activeColor)
            binding.guideBithumbTv.setTextColor(activeTextColor)
            binding.guideUpbitTv.setTextColor(inactiveTextColor)
            binding.upbitTabCv.setCardBackgroundColor(inactiveColor)
        } else {
            binding.guideUpbitTv.setTextColor(activeTextColor)
            binding.guideBithumbTv.setTextColor(inactiveTextColor)
            binding.upbitTabCv.setCardBackgroundColor(activeColor)
            binding.bithumbTabCv.setCardBackgroundColor(inactiveColor)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}