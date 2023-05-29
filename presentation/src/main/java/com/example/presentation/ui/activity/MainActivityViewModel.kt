package com.example.presentation.ui.activity

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.AuthorizeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val authorizeUseCase: AuthorizeUseCase)  : ViewModel() {

    var authorize: Boolean
        get() = authorizeUseCase.authorize
        set(value) {
            authorizeUseCase.authorize = value
        }
}