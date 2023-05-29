package com.example.data.repositories

import com.example.domain.repositories.SignInRepository
import com.example.domain.utils.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SignInRepositoryImpl(

) : SignInRepository {

    override fun getAuthState() =
        flow<Either<String, String>> {

        }.flowOn(Dispatchers.IO).catch {

        }

    override suspend fun firebaseSignIn() =
        flow<Either<String, String>> {

        }.flowOn(Dispatchers.IO).catch {

        }

    override suspend fun firebaseSignOut(): Flow<Either<String, String>> {

    }

}