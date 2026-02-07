package com.stable.scoi.presentation.ui.home.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.stable.scoi.R
import com.stable.scoi.databinding.DialogSelectNetworkBinding
import com.stable.scoi.domain.model.home.Network
import com.stable.scoi.presentation.ui.home.adapter.NetworkAdapter

class SelectNetworkDialogFragment: DialogFragment() {
    companion object {
        const val KEY_ACCOUNT = "KEY_ACCOUNT"
        const val KEY_NETWORK = "KEY_NETWORK"

        fun newInstance(
            account: String,
            networkList: List<String>
        ) = SelectNetworkDialogFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_ACCOUNT, account)
                putStringArrayList(KEY_NETWORK, ArrayList(networkList))
            }
        }
    }

    private val networkAdapter : NetworkAdapter by lazy {
        NetworkAdapter(object : NetworkAdapter.Delegate {
            override fun onClickItem(item: Network) {
                val currentList = networkAdapter.currentList

                val newList = currentList.map {
                    if (it.name == item.name) {
                        it.copy(isChecked = true)
                    } else {
                        it.copy(isChecked = false)
                    }
                }

                binding.buttonCreate.setBackgroundResource(R.drawable.bg_rect_active_fill_radius60)
                binding.buttonText.setTextColor(ContextCompat.getColor(requireActivity(), R.color.active))

                networkAdapter.submitList(newList)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    }

    private val account: String by lazy {
        arguments?.getString(KEY_ACCOUNT) ?: ""
    }

    private val networkList: List<String> by lazy {
        arguments?.getStringArrayList(KEY_NETWORK)?.toList() ?: emptyList()
    }

    private val binding: DialogSelectNetworkBinding by lazy {
        DialogSelectNetworkBinding.inflate(layoutInflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    private fun initView() {
        binding.apply {

            imageClose.setOnClickListener {
                dismiss()
            }

            recyclerNetwork.apply {
                adapter = networkAdapter
                layoutManager = LinearLayoutManager(context)
                itemAnimator = null
            }

            textNetwork.text = "$account 입금 네트워크"

            networkAdapter.submitList(networkList.map {
                Network(name = it, isChecked = false)
            })

            buttonCreate.setOnClickListener {
                dismiss()
            }
        }
    }
}