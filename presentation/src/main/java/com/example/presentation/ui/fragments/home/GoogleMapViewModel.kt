package com.example.presentation.ui.fragments.home

import android.Manifest
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.AddLocationUseCase
import com.example.domain.usecase.GetLocationUseCase
import com.example.presentation.utils.GpsStatusListener
import com.example.presentation.utils.LocationLiveData
import com.example.presentation.utils.PermissionStatusListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoogleMapViewModel @Inject constructor(
    private val getLocationUseCase: GetLocationUseCase,
    private val addLocationUseCase: AddLocationUseCase
) : ViewModel() {
    fun gpsStatusLiveData(application: FragmentActivity) = GpsStatusListener(application)
    fun locationPermissionStatusLiveData(application: FragmentActivity) =
        PermissionStatusListener(application, Manifest.permission.ACCESS_FINE_LOCATION)

    fun locationLiveData(application: FragmentActivity) = LocationLiveData(application)

    init {
        getLocations()
    }

    private fun getLocations() = viewModelScope.launch {
        getLocationUseCase.getLocations()
    }

    fun addLocation(lat: Double, lng: Double) = viewModelScope.launch {
        addLocationUseCase.addLocation(lat, lng)
    }
}