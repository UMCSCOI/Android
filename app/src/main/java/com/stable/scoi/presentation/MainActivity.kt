package com.stable.scoi.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.stable.scoi.R
import com.stable.scoi.databinding.ActivityMainBinding
import com.stable.scoi.extension.gone
import com.stable.scoi.extension.visible
import com.stable.scoi.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainActivityUiState, MainActivityEvent, MainActivityViewModel>(
    ActivityMainBinding::inflate
) {
    override val viewModel: MainActivityViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        window.navigationBarColor = Color.WHITE
        controller.isAppearanceLightNavigationBars = true
    }


    override fun initView() {
        binding.apply {
            vm = viewModel
            initNavigation()
        }
    }

    @SuppressLint("ResourceType")
    override fun initState() {
        repeatOnStarted {
            launch {
                viewModel.uiEvent.collect{
                    // TODO 이벤트 처리
                }
            }

            launch {
                viewModel.uiState.collect {
                    if (it.isHome) navController.navigate(R.id.homeFragment)
                    if (it.isCharge) navController.navigate(R.id.chargeFragment)
                    //if (it.isWallet) navController.navigate(R.layout.fragment_home)
                }
            }
        }
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.layoutBottomNav.visible()
                }
                R.id.chargeFragment -> {
                    binding.layoutBottomNav.visible()
                }
                else -> {
                    binding.layoutBottomNav.gone()
                }
                //R.layout.fragment_wallet -> {}
            }
        }
    }
}