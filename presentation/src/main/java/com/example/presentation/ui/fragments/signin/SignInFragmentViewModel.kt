package com.example.presentation.ui.fragments.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.AddLocationUseCase
import com.example.domain.usecase.AuthorizeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInFragmentViewModel @Inject constructor(
    private val authorizeUseCase: AuthorizeUseCase,
    private val addLocationUseCase: AddLocationUseCase
): ViewModel() {

    var authorize: Boolean
        get() = authorizeUseCase.authorize
        set(value) {
            authorizeUseCase.authorize = value
        }

    fun addLocation(lat: Double, lng: Double) = viewModelScope.launch {
        addLocationUseCase.addLocation(lat, lng)
    }
}