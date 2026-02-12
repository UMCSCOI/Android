package com.stable.scoi.presentation.ui.home

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.stable.scoi.databinding.FragmentHomeBinding
import com.stable.scoi.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.stable.scoi.R
import com.stable.scoi.extension.inVisible
import com.stable.scoi.extension.visible
import com.stable.scoi.presentation.ui.home.adapter.AccountCardAdapter
import com.stable.scoi.presentation.ui.home.dialog.CustomTypefaceSpan
import com.stable.scoi.presentation.ui.home.dialog.SelectAccountDialogFragment
import com.stable.scoi.presentation.ui.home.dialog.SelectNetworkDialogFragment
import com.stable.scoi.presentation.ui.home.dialog.SelectStableDialogFragment
import com.stable.scoi.util.SLOG

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeUiState, HomeEvent, HomeViewModel>(
    FragmentHomeBinding::inflate,
) {
    override val viewModel: HomeViewModel by viewModels()

    private val accountCardAdapter : AccountCardAdapter by lazy {
        AccountCardAdapter(object : AccountCardAdapter.Delegate {
            override fun onClickCard() {
                showDialog()
            }
        })
    }

    override fun initView() {
        binding.apply {
            vm = viewModel

            imgMyWhite.setOnClickListener {
                findNavController().navigate(R.id.myPageFragment)
            }
            val boldFont = ResourcesCompat.getFont(requireActivity(), R.font.pretendard_semibold)

            textTitle.text = buildSpannedString {
                inSpans(CustomTypefaceSpan(boldFont!!)) {
                    append("서희정")
                }
                append("님!\n송금을 시작해볼까요?")
            }
            textTitle2.text = buildSpannedString {
                inSpans(CustomTypefaceSpan(boldFont!!)) {
                    append("서희정")
                }
                append("님!\n어떤 자산을 보내시겠어요?")
            }

            setupViewPager()

            if (viewModel.isWalletOpened) {
                setFinalStateImmediate()
            } else {
                imageWalletFront.setOnClickListener { startAnimation() }
                layoutCard.setOnClickListener { startAnimation() }

                val white = ContextCompat.getColor(requireActivity(), R.color.white)
                requireActivity().findViewById<View>(R.id.main).setBackgroundColor(white)
                root.setBackgroundColor(white)
            }
        }
    }

    private fun setupViewPager() {
        binding.viewPagerCard.apply {
            adapter = accountCardAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            setCardPreviewStyle()
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (viewModel.uiState.value.accountList.isNotEmpty()) {
                        if (viewModel.uiState.value.accountList[position].isEmpty) {
                            binding.layoutSelect.isEnabled = false
                            binding.layoutSelect.setBackgroundResource(R.drawable.bg_rect_disable_fill_radius60)
                            binding.textSelect.setTextColor(ContextCompat.getColor(requireActivity(),R.color.disabled))
                        } else {
                            binding.layoutSelect.isEnabled = true
                            binding.layoutSelect.setBackgroundResource(R.drawable.bg_rect_skyblue_radius60)
                            binding.textSelect.setTextColor(ContextCompat.getColor(requireActivity(),R.color.active))
                        }
                        binding.textWalletKey.text = if(viewModel.uiState.value.accountList[position].isEmpty) "입금 주소가 아직 생성되지 않았어요." else viewModel.uiState.value.accountList[position].key
                        viewModel.setSelectPosition(position)
                    }
                }
            })
            binding.dotsIndicator.attachTo(this)
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiEvent.collect {
                    handleEvent(it)
                }
            }

            launch {
                viewModel.uiState.collect {
                    accountCardAdapter.submitList(it.accountList)
                }
            }
        }
    }

    private fun handleEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.MoveToTransferEvent -> {
                showSelectDialog()
            }
        }
    }

    private fun navigateToTransfer() {
        val action = HomeFragmentDirections.actionHomeFragmentToTansferFragment()
        findNavController().navigate(action)
    }

    private fun startAnimation() {
        if (viewModel.isWalletOpened) return
        viewModel.isWalletOpened = true

        val screenHeight = binding.root.height.toFloat()

        val viewsToHide = listOf(binding.imgMyBlack, binding.textTouchWallet, binding.textTitle)
        viewsToHide.forEach { view ->
            view.animate()
                .alpha(0f)
                .setDuration(600)
                .withEndAction {
                    view.visibility = View.INVISIBLE
                    view.alpha = 1f
                }
                .start()
        }

        val viewsToShow = listOf(binding.imgMyWhite, binding.textWalletKey, binding.layoutSelect, binding.dotsIndicator, binding.textTitle2)
        viewsToShow.forEach { view ->
            view.alpha = 0f
            view.visibility = View.VISIBLE
            view.animate().alpha(1f).setDuration(600).start()
        }

        val walletGroup = listOf(binding.imageWalletBehind, binding.imageWalletFront)
        walletGroup.forEach { view ->
            view.animate()
                .translationY(screenHeight)
                .setDuration(600)
                .setInterpolator(AccelerateInterpolator())
                .withEndAction { view.visibility = View.GONE }
                .start()
        }

        animateColors()
    }

    private fun animateColors() {
        val bgStartColor = Color.WHITE
        val bgEndColor = ContextCompat.getColor(requireActivity(), R.color.active)
        val textStartColor = ContextCompat.getColor(requireActivity(), R.color.black_m3)
        val textEndColor = Color.WHITE

        val activityRootView = requireActivity().findViewById<View>(R.id.main)
        val evaluator = ArgbEvaluator()

        ValueAnimator.ofObject(evaluator, bgStartColor, bgEndColor).apply {
            duration = 600
            addUpdateListener { animator ->
                val fraction = animator.animatedFraction
                val currentColor = animator.animatedValue as Int
                binding.root.setBackgroundColor(currentColor)
                activityRootView.setBackgroundColor(currentColor)

                val currentTextColor = evaluator.evaluate(fraction, textStartColor, textEndColor) as Int
                binding.title.setTextColor(currentTextColor)
                binding.textTitle.setTextColor(currentTextColor)
            }
            doOnEnd {
                binding.layoutCard.inVisible()
                binding.viewPagerCard.visible()
            }
            start()
        }
    }

    private fun setFinalStateImmediate() {
        val activeColor = ContextCompat.getColor(requireActivity(), R.color.active)
        val whiteColor = Color.WHITE
        val activityRootView = requireActivity().findViewById<View>(R.id.main)

        binding.apply {
            imgMyBlack.visibility = View.GONE
            textTouchWallet.visibility = View.GONE

            imageWalletBehind.visibility = View.GONE
            imageWalletFront.visibility = View.GONE

            imgMyWhite.visibility = View.VISIBLE
            imgMyWhite.alpha = 1f

            textWalletKey.visibility = View.VISIBLE
            textWalletKey.alpha = 1f

            layoutSelect.visibility = View.VISIBLE
            layoutSelect.alpha = 1f

            layoutCard.inVisible()
            viewPagerCard.visible()

            root.setBackgroundColor(activeColor)
            activityRootView.setBackgroundColor(activeColor)

            title.setTextColor(whiteColor)
            textTitle.setTextColor(whiteColor)
        }
    }

    private fun setCardPreviewStyle() {
        with(binding.viewPagerCard) {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 1

            val pageMarginPx = 12.dpToPx()
            val marginTransformer = MarginPageTransformer(pageMarginPx)
            setPageTransformer(marginTransformer)
        }
    }
    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    override fun onPause() {
        super.onPause()
        val activityRootView = requireActivity().findViewById<View>(R.id.main)
        activityRootView.setBackgroundColor(Color.WHITE)
    }

    private fun showDialog() {
        childFragmentManager.setFragmentResultListener("requestKey_coin", viewLifecycleOwner) { requestKey, bundle ->

            val result = bundle.getString("bundleKey_coin")

            result?.let { coinName ->
                val networkList = when (coinName) {
                    "USDT" -> listOf(
                        "트론 (Tron)",
                        "이더리움 (Ethereum)",
                        "카이아 (Kaia)",
                        "앱토스 (Aptos)"
                    )
                    "USDC" -> listOf(
                        "이더리움 (Ethereum)"
                    )
                    else -> listOf("이더리움 (Ethereum)", "폴리곤 (Polygon)")
                }
                showNetworkDialg(coinName, networkList)
            }

        }
        SelectAccountDialogFragment().show(childFragmentManager, "")
    }

    private fun showNetworkDialg(coin: String, list: List<String>) {
        val dialog = SelectNetworkDialogFragment.newInstance(coin, list)

        dialog.onNetworkSelectedListener = { selectedNetworkName ->
            viewModel.createAddress(coin,selectedNetworkName)
        }

        dialog.show(parentFragmentManager, "SelectNetworkDialog")
    }

    private fun showSelectDialog() {
        childFragmentManager.setFragmentResultListener("requestKey_coin", viewLifecycleOwner) { requestKey, bundle ->

            val result = bundle.getString("bundleKey_coin")
            //TODO 데이터 담아서 보내기
            navigateToTransfer()
        }
        SelectStableDialogFragment().show(childFragmentManager, "")
    }
}