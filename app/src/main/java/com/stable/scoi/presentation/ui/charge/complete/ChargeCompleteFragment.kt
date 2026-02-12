package com.stable.scoi.presentation.ui.charge.complete

import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.stable.scoi.R
import com.stable.scoi.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.stable.scoi.databinding.FragmentChargeCompleteBinding
import com.stable.scoi.presentation.base.UiState
import kotlin.getValue

@AndroidEntryPoint
class ChargeCompleteFragment : BaseFragment<FragmentChargeCompleteBinding, UiState, ChargeCompleteEvent, ChargeCompleteViewModel>(
    FragmentChargeCompleteBinding::inflate,
) {
    override val viewModel: ChargeCompleteViewModel by viewModels()

    private val args: ChargeCompleteFragmentArgs by navArgs()

    override fun initView() {
        binding.apply {
            vm = viewModel
            val countVal = args.coinCount.replace(",", "").toDoubleOrNull() ?: 0.0
            val formattedCount = java.text.DecimalFormat("#,###.##").format(countVal)

            textCount.text = "$formattedCount ${args.coin}"
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
        }
    }

    private fun handleEvent(event: ChargeCompleteEvent) {
        when (event) {
            ChargeCompleteEvent.MoveToChargeMain -> navigateToChargeMain()
            ChargeCompleteEvent.MoveToWallet -> navigateToWallet()
        }
    }

    private fun navigateToWallet() {
        val action = ChargeCompleteFragmentDirections.actionCompleteToMyWallet()

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.main_graph, true)
            .build()

        findNavController().navigate(action, navOptions)
    }

    private fun navigateToChargeMain() {
        val action = ChargeCompleteFragmentDirections.actionCompleteToChargeMain()

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.main_graph, true)
            .build()

        findNavController().navigate(action, navOptions)
    }
}