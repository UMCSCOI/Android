package com.stable.scoi.util

import android.util.Log

object SLOG {

    fun D(msg : String) {
        Log.d("SCOI", msg)
    }

    fun D(log : String, msg : String) {
        Log.d(log, msg)
    }
}
