package com.stable.scoi.presentation.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.stable.scoi.R


class SimplePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_transfer_password)
    }
}