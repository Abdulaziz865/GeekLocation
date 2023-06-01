package com.example.domain.usecase

import com.example.domain.repositories.LocationRepository
import javax.inject.Inject

class AddLocationUseCase @Inject constructor(private val locationRepository: LocationRepository) {
//
//    suspend fun addLocation(lat: Double, lng: Double) {
//        locationRepository.addLocationToFirestore(lat, lng)
//    }
}