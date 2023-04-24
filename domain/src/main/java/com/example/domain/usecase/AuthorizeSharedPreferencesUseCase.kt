package com.example.domain.usecase

import com.example.domain.repositories.AuthorizeSharedPreferencesRepository
import javax.inject.Inject

class AuthorizeSharedPreferencesUseCase @Inject constructor(private val repository: AuthorizeSharedPreferencesRepository){

    fun saveDataAuthorize(key: String , value : Boolean)
    = repository.saveDataAuthorize(key, value)

    fun getDataAuthorize(key: String) = repository.getDataAuthorize(key)
}