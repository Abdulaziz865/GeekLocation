package com.example.domain.usecase

import com.example.domain.repositories.FirestoreRepository
import javax.inject.Inject

class UserAvailableUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {

    operator fun invoke() = firestoreRepository.userAvailable()
}