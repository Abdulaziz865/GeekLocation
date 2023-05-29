package com.example.domain.usecase

import com.example.domain.repositories.AuthorizeRepository
import javax.inject.Inject

class AuthorizeUseCase @Inject constructor(private val authorizeRepository: AuthorizeRepository) {

    var authorize: Boolean
        get() = authorizeRepository.authorize
        set(value) {
            authorizeRepository.authorize = value
        }
}