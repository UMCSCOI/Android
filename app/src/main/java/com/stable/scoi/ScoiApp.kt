package com.stable.scoi

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ScoiApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }

}