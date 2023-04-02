package com.example.geeklocation

import android.app.Application
import com.example.presentation.utils.SharedPreferenceUtil

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPreferenceUtil.units(this)
    }
}