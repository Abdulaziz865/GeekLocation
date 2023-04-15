package com.example.presentation.ui.activity

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.AuthorizeSharedPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val authorizeSharedPreferencesUseCase: AuthorizeSharedPreferencesUseCase)  : ViewModel() {

    fun getDataAuthorize(key: String) = authorizeSharedPreferencesUseCase.getDataAuthorize(key)
}