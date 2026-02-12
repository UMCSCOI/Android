package com.stable.scoi.presentation.ui.Auth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.databinding.ItemGuideBinding
import com.stable.scoi.presentation.ui.guide.model.GuideStep

class GuideAdapter : ListAdapter<GuideStep, GuideAdapter.GuideViewHolder>(GuideDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val binding = ItemGuideBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GuideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class GuideViewHolder(private val binding: ItemGuideBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GuideStep) {
            with(binding) {
                guideStepNumber.text = item.stepNum
                guideTitleTv.text = item.title
                guideTextTv.text = item.description

                guideImageIv.setImageResource(item.imageRes)
            }
        }
    }
}

class GuideDiffCallback : DiffUtil.ItemCallback<GuideStep>() {
    override fun areItemsTheSame(oldItem: GuideStep, newItem: GuideStep): Boolean {
        return oldItem.stepNum == newItem.stepNum
    }

    override fun areContentsTheSame(oldItem: GuideStep, newItem: GuideStep): Boolean {
        return oldItem == newItem
    }
}