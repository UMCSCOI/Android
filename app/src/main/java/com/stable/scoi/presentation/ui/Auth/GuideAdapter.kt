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
import com.stable.scoi.R
import com.stable.scoi.databinding.ItemGuideBinding
import com.stable.scoi.presentation.ui.guide.model.GuideStep

class GuideAdapter(
    private val onExchangeClick: (String) -> Unit
) : ListAdapter<GuideStep, GuideAdapter.GuideViewHolder>(GuideDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val binding = ItemGuideBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GuideViewHolder(binding, onExchangeClick)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
        class GuideViewHolder(
            private val binding: ItemGuideBinding,
            private val onExchangeClick: (String) -> Unit
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(item: GuideStep) {
                val context = binding.root.context

                with(binding) {
                    guideStepNumber.text = item.stepNum
                    guideTitleTv.text = item.title
                    guideTextTv.text = item.description
                    guideImageIv.setImageResource(item.imageRes)

                    val isBithumb = item.exchangeType == "BITHUMB"
                    updateTabUI(context, isBithumb)

                    bithumbTabCv.setOnClickListener { onExchangeClick("BITHUMB") }
                    upbitTabCv.setOnClickListener { onExchangeClick("UPBIT") }

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
                        guideNoticeCv.visibility = View.GONE
                    }
                }
            }

            private fun ItemGuideBinding.updateTabUI(context: Context, isBithumb: Boolean) {
                val activeColor = context.getColor(R.color.active_fill)
                val activeTextColor = context.getColor(R.color.active)
                val inactiveTextColor = context.getColor(R.color.disabled)
                val transparent = android.graphics.Color.TRANSPARENT

                if (isBithumb) {
                    bithumbTabCv.setCardBackgroundColor(activeColor)
                    guideBithumbTv.setTextColor(activeTextColor)
                    upbitTabCv.setCardBackgroundColor(transparent)
                    guideUpbitTv.setTextColor(inactiveTextColor)
                } else {
                    upbitTabCv.setCardBackgroundColor(activeColor)
                    guideUpbitTv.setTextColor(activeTextColor)
                    bithumbTabCv.setCardBackgroundColor(transparent)
                    guideBithumbTv.setTextColor(inactiveTextColor)
                }
            }
        }
    }

        private fun copyToClipboard(context: Context, text: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("IP_ADDRESS", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "IP 주소가 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }

class GuideDiffCallback : DiffUtil.ItemCallback<GuideStep>() {
    override fun areItemsTheSame(oldItem: GuideStep, newItem: GuideStep): Boolean = oldItem.stepNum == newItem.stepNum
    override fun areContentsTheSame(oldItem: GuideStep, newItem: GuideStep): Boolean = oldItem == newItem
}