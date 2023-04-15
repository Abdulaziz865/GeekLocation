package com.example.data.repositories

import android.content.SharedPreferences
import com.example.domain.repositories.AuthorizeSharedPreferencesRepository
import javax.inject.Inject

class AuthorizeAuthorizeSharedPreferencesRepositoryImpl @Inject constructor(private val sharedPreferences: SharedPreferences) :
    AuthorizeSharedPreferencesRepository {

    override fun saveDataAuthorize(KEY_NAME: String, value: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(KEY_NAME, value)
        editor.apply()
    }

    override fun getDataAuthorize(KEY_NAME: String): Boolean {
        return sharedPreferences.getBoolean(KEY_NAME, false)
    }
}