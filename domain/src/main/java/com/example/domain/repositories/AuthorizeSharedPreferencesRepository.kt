package com.example.domain.repositories

interface AuthorizeSharedPreferencesRepository {

    fun saveDataAuthorize(KEY_NAME: String , value : Boolean)

    fun getDataAuthorize(KEY_NAME: String) : Boolean
}