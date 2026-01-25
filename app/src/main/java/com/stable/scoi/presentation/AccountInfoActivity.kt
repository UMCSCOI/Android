package com.stable.scoi.presentation

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.stable.scoi.R

class AccountInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_info)

        val btnPersonal = findViewById<MaterialCardView>(R.id.btnPersonal)
        val btnCorporate = findViewById<MaterialCardView>(R.id.btnCorporate)
        val tvPersonal = findViewById<TextView>(R.id.tvPersonal)
        val tvCorporate = findViewById<TextView>(R.id.tvCorporate)

        setActivated(btnPersonal, tvPersonal, true)
        setActivated(btnCorporate, tvCorporate, false)

        btnPersonal.setOnClickListener {
            setActivated(btnPersonal, tvPersonal, true)
            setActivated(btnCorporate, tvCorporate, false)
        }

        btnCorporate.setOnClickListener {
            setActivated(btnPersonal, tvPersonal, false)
            setActivated(btnCorporate, tvCorporate, true)
        }
    }

    private fun setActivated(card: MaterialCardView, text: TextView, isActivated: Boolean) {
        if (isActivated) {
            card.setCardBackgroundColor(Color.parseColor("#E8EFFF"))
            card.strokeColor = Color.parseColor("#2569F2")
            text.setTextColor(Color.parseColor("#2569F2"))
            text.paint.isFakeBoldText = true
        } else {
            card.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            card.strokeColor = Color.parseColor("#D1D1D1")
            text.setTextColor(Color.parseColor("#9E9E9E"))
            text.paint.isFakeBoldText = false
        }
    }
}