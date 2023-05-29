package com.example.domain.usecase

import com.example.domain.repositories.LocationRepository
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(private val locationRepository: LocationRepository) {
//
//    suspend fun getLocations() {
//        locationRepository.getLocationsFromFirestore().collect{Response.Loading}
//    }
}