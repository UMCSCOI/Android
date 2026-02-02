package com.stable.scoi.presentation.base.componant

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.stable.scoi.R

class ScoiButton @JvmOverloads constructor(
    mContext: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(mContext, attrs, defStyle) {

    private val card: MaterialCardView
    private val label: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_scoi_button, this, true)
        card = findViewById(R.id.card)
        label = findViewById(R.id.label)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ScoiButton, defStyle, 0)

        try {
            // 텍스트
            label.text = a.getString(R.styleable.ScoiButton_buttonText) ?: label.text
            label.setTextColor(
                a.getColor(R.styleable.ScoiButton_textColor, label.currentTextColor)
            )
            label.setTextAppearance(
                a.getResourceId(R.styleable.ScoiButton_textAppearance, R.style.l1m)
            )

            // 배경색
            card.setCardBackgroundColor(
                a.getColor(R.styleable.ScoiButton_backgroundColor, resources.getColor(R.color.white))
            )

            // 코너
            card.radius = a.getDimension(R.styleable.ScoiButton_cornerRadius, 60.toFloat())

            // Border
            card.strokeWidth = a.getDimensionPixelSize(R.styleable.ScoiButton_borderWidth, 0)
            card.strokeColor = a.getColor(R.styleable.ScoiButton_borderColor, Color.TRANSPARENT)

        } finally {
            a.recycle()
        }
    }
}