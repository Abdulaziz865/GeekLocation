package com.example.presentation.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceUtil {

    private lateinit var preferences : SharedPreferences

    fun units(context: Context){
        preferences = context.getSharedPreferences("name" , Context.MODE_PRIVATE)
    }

    var isPreference: Boolean
        get() = preferences.getBoolean("preference", false)
        set(value) = preferences.edit().putBoolean("preference", value).apply()
}