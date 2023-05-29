package com.example.data.repositories

import com.example.data.locale.helper.AuthorizePreferenceHelper
import com.example.domain.repositories.AuthorizeRepository
import javax.inject.Inject

class AuthorizeRepositoryImpl @Inject constructor(private val authorizePreferenceHelper: AuthorizePreferenceHelper) :
    AuthorizeRepository {

    override var authorize: Boolean
        get() = authorizePreferenceHelper.authorize
        set(value) {
            authorizePreferenceHelper.authorize = value
        }
}