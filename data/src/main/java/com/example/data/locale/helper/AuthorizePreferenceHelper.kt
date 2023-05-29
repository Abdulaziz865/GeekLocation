package com.example.data.locale.helper

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorizePreferenceHelper @Inject constructor(@ApplicationContext context: Context) {

    private val preference: SharedPreferences =
        context.getSharedPreferences("preferences" , Context.MODE_PRIVATE)

    var authorize: Boolean
        get() = preference.getBoolean("authorize" , false)
        set(value) = preference.edit().putBoolean("authorize" , value).apply()
}