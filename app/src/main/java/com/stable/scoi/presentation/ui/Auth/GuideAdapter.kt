package com.stable.scoi.presentation.ui.Auth

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stable.scoi.databinding.ItemGuideBinding // XML 이름에 맞춰 확인해줘!
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
            val context = binding.root.context

            with(binding) {
                guideStepNumber.text = item.stepNum
                guideTitleTv.text = item.title
                guideTextTv.text = item.description
                guideImageIv.setImageResource(item.imageRes)

                if (!item.ipAddress.isNullOrEmpty()) {
                    guideIpBoxLayout.visibility = View.VISIBLE
                    guideIpTv.text = item.ipAddress

                    guideIpCopyIv.setOnClickListener {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("IP_ADDRESS", item.ipAddress)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "IP 주소가 복사되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    guideIpBoxLayout.visibility = View.GONE
                }

                if (!item.noticeText.isNullOrEmpty()) {
                    guideNoticeCv.visibility = View.VISIBLE
                    guideExplainTv.text = item.noticeText
                } else {
                    // 데이터가 없으면 숨깁니다.
                    guideNoticeCv.visibility = View.GONE
                }
            }
        }

        // 클립보드 복사 함수
        private fun copyToClipboard(context: Context, text: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("IP_ADDRESS", text)
            clipboard.setPrimaryClip(clip)

            // 유저 피드백 (토스트)
            Toast.makeText(context, "IP 주소가 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}

class GuideDiffCallback : DiffUtil.ItemCallback<GuideStep>() {
    override fun areItemsTheSame(oldItem: GuideStep, newItem: GuideStep): Boolean {
        // stepNum이 유니크한 값이면 이걸로 비교!
        return oldItem.stepNum == newItem.stepNum
    }

    override fun areContentsTheSame(oldItem: GuideStep, newItem: GuideStep): Boolean {
        return oldItem == newItem
    }
}