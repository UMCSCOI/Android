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

    private val guideAdapter by lazy {
        GuideAdapter { exchangeType ->
            loadGuideData(exchangeType)
        }
    }

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

        loadGuideData("BITHUMB")
    }

    private fun setupViewPager() {
        binding.guideViewPager.adapter = guideAdapter

        binding.guideViewPager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val isLastPage = position == guideAdapter.itemCount - 1
                binding.apiKeyInputTv.visibility = if (isLastPage) View.VISIBLE else View.GONE
            }
        })
    }

    private fun setupIndicator() {
        com.google.android.material.tabs.TabLayoutMediator(binding.guideIndicator, binding.guideViewPager) { _, _ ->
        }.attach()
    }

    private fun loadGuideData(type: String) {
        val data = if (type == "BITHUMB") {
            listOf(
                GuideStep("01", "거래소 API 페이지 접속", getString(R.string.bithumb_1), R.drawable.bithumb_1, "* API 설정은 보안상 PC 웹 환경에서만 가능합니다.",null,"BITHUMB"),
                GuideStep("01", "거래소 API 페이지 접속", getString(R.string.bithumb_2), R.drawable.bithumb_2, "* API 설정은 보안상 PC 웹 환경에서만 가능합니다.",null,"BITHUMB"),
                GuideStep("02", "API 활성 항목 선택", getString(R.string.guide_2), R.drawable.r_2,null,null,"BITHUMB"),
                GuideStep("03", "IP 주소 등록", getString(R.string.guide_3), R.drawable.r_3,null, "13.209.12.10","BITHUMB"),
                GuideStep("04", "동의하기", getString(R.string.guide_4), R.drawable.r_4,null,null,"BITHUMB"),
                GuideStep("05", "API KEY 발급", getString(R.string.guide_5), R.drawable.r_5, "* SECRET KEY는 최초 1회만 확인 가능하므로 꼭 보관해 주세요.",null,"BITHUMB")
            )
        } else {
            listOf(
                GuideStep("01", "거래소 API 페이지 접속", getString(R.string.upbit_1), R.drawable.upbit_1, "* API 설정은 보안상 PC 웹 환경에서만 가능합니다.",null,"UPBIT"),
                GuideStep("01", "거래소 API 페이지 접속", getString(R.string.upbit_2), R.drawable.upbit_2, "* API 설정은 보안상 PC 웹 환경에서만 가능합니다.",null,"UPBIT"),
                GuideStep("02", "API 활성 항목 선택", getString(R.string.guide_2), R.drawable.r_2, "* 스코이는 사용자의 자산 보호를 최우선으로 합니다.",null,"UPBIT"),
                GuideStep("03", "IP 주소 등록", getString(R.string.guide_3), R.drawable.r_3, null, "13.209.12.10","UPBIT"),
                GuideStep("04", "동의하기", getString(R.string.guide_4), R.drawable.r_4,null,null,"UPBIT"),
                GuideStep("05", "API KEY 발급", getString(R.string.guide_5), R.drawable.r_5, "* SECRET KEY는 최초 1회만 확인 가능하므로 꼭 보관해 주세요.",null,"UPBIT")
            )
        }

        guideAdapter.submitList(data)
        binding.guideViewPager.post { binding.guideViewPager.setCurrentItem(0, false) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}