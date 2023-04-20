package com.example.presentation.ui.fragments

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.example.presentation.utils.GpsStatusListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoogleMapViewModel @Inject constructor() : ViewModel() {

    fun gpsStatusLiveData(application: FragmentActivity) = GpsStatusListener(application)
}