package com.example.presentation.ui.fragments.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.AddLocationUseCase
import com.example.domain.usecase.AuthorizeUseCase
import com.example.domain.usecase.UserAvailableUseCase
import com.example.domain.utils.Either
import com.example.presentation.state.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInFragmentViewModel @Inject constructor(
    private val authorizeUseCase: AuthorizeUseCase,
    private val addLocationUseCase: AddLocationUseCase,
    private val userAvailableUseCase: UserAvailableUseCase
): ViewModel() {

    var authorize: Boolean
        get() = authorizeUseCase.authorize
        set(value) {
            authorizeUseCase.authorize = value
        }

    private val _userAvailableState = MutableStateFlow<UIState<Boolean>>(UIState.Loading())
    val userAvailable = _userAvailableState.asStateFlow()

    init {
        userAvailable()
    }

    private fun userAvailable() {
        viewModelScope.launch {
            userAvailableUseCase.invoke().collect { either ->
                when (either) {
                    is Either.Left -> {
                        _userAvailableState.value = UIState.Error(either.message ?: "Error")
                    }
                    is Either.Right -> {
                        _userAvailableState.value = either.data?.let { UIState.Success(it) }!!
                    }
                }
            }
        }
    }
//
//    fun addLocation(lat: Double, lng: Double) = viewModelScope.launch {
//        addLocationUseCase.addLocation(lat, lng)
//    }
}