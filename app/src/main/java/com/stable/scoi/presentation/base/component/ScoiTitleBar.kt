package com.stable.scoi.presentation.base.component

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import com.stable.scoi.R

class ScoiTitleBar @JvmOverloads constructor(
    mContext: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(mContext, attrs, defStyle) {

    internal val label: TextView
    internal val back: ImageView
    internal val my: ImageView
    init {
        LayoutInflater.from(context).inflate(R.layout.custom_top_bar, this, true)
        label = findViewById(R.id.title)
        back = findViewById(R.id.img_back)
        my = findViewById(R.id.img_my)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ScoiTitleBar, defStyle, 0)

        try {
            label.visibility = View.GONE
            back.visibility = View.GONE
            my.visibility = View.GONE
        } finally {
            a.recycle()
        }
    }
}

@BindingAdapter("title")
fun bindScoiTitleBarTitle(view: ScoiTitleBar, title: String?) {
    val hasTitle = !title.isNullOrBlank()
    view.label.visibility = if (hasTitle) View.VISIBLE else View.GONE
    if (hasTitle) view.label.text = title
}

@BindingAdapter("leftClick")
fun bindScoiTitleBarLeftClick(view: ScoiTitleBar, listener: View.OnClickListener?) {
    val hasClick = listener != null
    view.back.visibility = if (hasClick) View.VISIBLE else View.GONE
    view.back.setOnClickListener(listener)
}

@BindingAdapter("rightClick")
fun bindScoiTitleBarRightClick(view: ScoiTitleBar, listener: View.OnClickListener?) {
    val hasClick = listener != null
    view.my.visibility = if (hasClick) View.VISIBLE else View.GONE
    view.my.setOnClickListener(listener)
}