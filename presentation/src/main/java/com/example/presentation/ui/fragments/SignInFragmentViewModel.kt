package com.example.presentation.ui.fragments

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.AuthorizeSharedPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInFragmentViewModel @Inject constructor(private val authorizeSharedPreferencesUseCase: AuthorizeSharedPreferencesUseCase): ViewModel() {

    fun saveDataAuthorize(KEY_NAME: String, value: Boolean) = authorizeSharedPreferencesUseCase.saveDataAuthorize(KEY_NAME, value )
}