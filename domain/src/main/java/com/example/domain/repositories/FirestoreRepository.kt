package com.example.domain.repositories

import com.example.domain.utils.Either
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    fun userAvailable(): Flow<Either<String, Boolean>>

//    suspend fun addDocument(document: Document): Result<Unit>
}