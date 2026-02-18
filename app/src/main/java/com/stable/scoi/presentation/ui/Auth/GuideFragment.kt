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

        binding.apiKeyInputTv.visibility=View.GONE

        loadGuideData("BITHUMB")
        updateTabUI(isBithumb = true)
    }


    private fun setupViewPager() {
        binding.guideViewPager.adapter = guideAdapter

        binding.guideViewPager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val isLastPage = position == guideAdapter.itemCount - 1

                if (isLastPage) {
                    binding.apiKeyInputTv.visibility = View.VISIBLE
                } else {
                    binding.apiKeyInputTv.visibility = View.GONE
                }
            }
        })
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
                GuideStep("01", "거래소 API 페이지 접속", getString(R.string.bithumb_1), R.drawable.bithumb_1,"* API 설정은 보안상 PC 웹 환경에서만 가능합니다."),
                GuideStep("01", "거래소 API 페이지 접속", getString(R.string.bithumb_2), R.drawable.bithumb_2,"* API 설정은 보안상 PC 웹 환경에서만 가능합니다."),
                GuideStep("02", "API 활성 항목 선택", getString(R.string.guide_2), R.drawable.r_2),
                GuideStep("03", "IP 주소 등록", getString(R.string.guide_3), R.drawable.r_3,"* 해당 주소는 스코이 서버 IP 주소로, 서비스 이용을 위해 등록이 필요합니다.","13.209.12.10"),
                GuideStep("04", "동의하기", getString(R.string.guide_4), R.drawable.r_4),
                GuideStep("05", "API KEY 발급", getString(R.string.guide_5), R.drawable.r_5,"* 중요 안내\n" +
                        "SECRET KEY는 생성 시 최초 1회만 확인할 수 있으며, 이후에는 다시 확인할 수 없습니다. 반드시 안전하게 보관해 주세요.")
            )
        } else {
            listOf(
                GuideStep("01", "거래소 API 페이지 접속", getString(R.string.upbit_1), R.drawable.upbit_1,"* API 설정은 보안상 PC 웹 환경에서만 가능합니다."),
                GuideStep("01", "거래소 API 페이지 접속", getString(R.string.upbit_2), R.drawable.upbit_2,"* API 설정은 보안상 PC 웹 환경에서만 가능합니다."),
                GuideStep("02", "API 활성 항목 선택", getString(R.string.guide_2), R.drawable.r_2,"* 스코이는 사용자의 자산 보호를 최우선으로 합니다.\n" +
                        "API를 통해 수행되는 모든 거래 및 출금 요청은 사용자 동의 하에만실행되며, 비정상적이거나 의도되지 않은 거래는 발생하지 않도록 설계하였습니다."),
                GuideStep("03", "IP 주소 등록", getString(R.string.guide_3), R.drawable.r_3,"* 해당 주소는 스코이 서버 IP 주소로, 서비스 이용을 위해 등록이 필요합니다.","13.209.12.10"),
                GuideStep("04", "동의하기", getString(R.string.guide_4), R.drawable.r_4),
                GuideStep("05", "API KEY 발급", getString(R.string.guide_5), R.drawable.r_5,"* 중요 안내\n" +
                        "SECRET KEY는 생성 시 최초 1회만 확인할 수 있으며, 이후에는 다시 확인할 수 없습니다. 반드시 안전하게 보관해 주세요.")
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