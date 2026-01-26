package com.stable.scoi.presentation

import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.stable.scoi.R
import com.stable.scoi.databinding.ActivityMainBinding
import com.stable.scoi.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainActivityUiState, MainActivityEvent, MainActivityViewModel>(
    ActivityMainBinding::inflate
) {
    override val viewModel: MainActivityViewModel by viewModels()
    private lateinit var navController: NavController


    override fun initView() {
        binding.apply {
            vm = viewModel
            initNavigation()
        }
    }

    override fun initState() {
        repeatOnStarted {
            launch {
                viewModel.uiEvent.collect{
                    // TODO 이벤트 처리
                }
            }

            launch {
                viewModel.uiState.collect {
                    // TODO 상태 관리
                }
            }
        }
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//
//            }
        }
    }
}