package com.example.domain.repositories

import com.example.domain.utils.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SignInRepository {

    fun getAuthState(): Flow<Either<String, String>>

    suspend fun firebaseSignIn(): Flow<Either<String, String>>
//
//    suspend fun firebaseSignOut(): Flow<Either<String, String>>

}