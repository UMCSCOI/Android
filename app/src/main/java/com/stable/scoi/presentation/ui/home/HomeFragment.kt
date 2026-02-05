package com.stable.scoi.presentation.ui.home

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
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
import com.stable.scoi.util.SLOG

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeUiState, HomeEvent, HomeViewModel>(
    FragmentHomeBinding::inflate,
) {
    private var flag: Boolean = false
    override val viewModel: HomeViewModel by viewModels()

    private val accountCardAdapter : AccountCardAdapter by lazy {
        AccountCardAdapter()
    }

    override fun initView() {
        binding.apply {
            vm = viewModel

            requireActivity().findViewById<View>(R.id.main).setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))

            viewPagerCard.apply {
                adapter = accountCardAdapter
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
                setCardPreviewStyle()
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)

                        textWalletKey.text = viewModel.uiState.value.accountList[position].key
                    }
                })
                dotsIndicator.attachTo(viewPagerCard)
            }

            imageWalletFront.setOnClickListener {
                startAnimation()
            }
            layoutCard.setOnClickListener {
                startAnimation()
            }
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
                navigateToTransfer()
            }
        }
    }

    private fun navigateToTransfer() {
        val action = HomeFragmentDirections.actionHomeFragmentToTansferFragment()
        findNavController().navigate(action)
    }

    private fun startAnimation() {
        if (flag) return
        flag = true
        val screenHeight = binding.root.height.toFloat()
        val walletGroup = listOf(
            binding.imageWalletBehind,
            binding.imageWalletFront,
        )
        val viewsToHide = listOf(
            binding.imgMyBlack,
            binding.textTouchWallet
        )

        viewsToHide.forEach { view ->
            view.animate()
                .alpha(0f)
                .setDuration(600)
                .withEndAction {
                    view.visibility = View.GONE
                    view.alpha = 1f
                }
                .start()
        }

        val viewsToShow = listOf(
            binding.imgMyWhite,
            binding.textWalletKey,
            binding.layoutSelect
        )

        viewsToShow.forEach { view ->
            view.alpha = 0f
            view.visibility = View.VISIBLE

            view.animate()
                .alpha(1f)
                .setDuration(600)
                .start()
        }

        val activityRootView = requireActivity().findViewById<View>(R.id.main)

        walletGroup.forEach { view ->
            view.animate()
                .translationY(screenHeight)
                .setDuration(600)
                .setInterpolator(AccelerateInterpolator())
                .withEndAction { view.visibility = View.GONE }
                .start()
        }

        val bgStartColor = Color.WHITE
        val bgEndColor = ContextCompat.getColor(requireActivity(), R.color.active)

        val textStartColor = ContextCompat.getColor(requireActivity(), R.color.black_m3)
        val textEndColor = Color.WHITE

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

            start()

            doOnEnd {
                binding.layoutCard.inVisible()
                binding.viewPagerCard.visible()
            }
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

}