package com.stable.scoi.presentation.ui.splash

import android.animation.Animator
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.stable.scoi.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.stable.scoi.R
import com.stable.scoi.databinding.FragmentSplashBinding

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashUiState, SplashUiEvent, SplashViewModel>(
    FragmentSplashBinding::inflate,
) {
    override val viewModel: SplashViewModel by viewModels()

    override fun initView() {
        binding.apply {
            vm = viewModel

            lotti.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationCancel(animation: Animator) {
                    //
                }

                override fun onAnimationEnd(animation: Animator) {
                    navigateToHome()
                }

                override fun onAnimationRepeat(animation: Animator) {
                    //TODO("Not yet implemented")
                }

                override fun onAnimationStart(animation: Animator) {
                    //TODO("Not yet implemented")
                }
            })
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

                }
            }
        }
    }

    private fun handleEvent(event: SplashUiEvent) {
        when (event) {
            SplashUiEvent.MoveToHomeEvent -> TODO()
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.homeFragment)
    }
}