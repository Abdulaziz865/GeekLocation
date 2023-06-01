package com.example.data.repositories

import com.example.domain.models.UserModel
import com.example.domain.repositories.FirestoreRepository
import com.example.domain.utils.Either
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepositoryImpl @Inject constructor(
    private val collectionRef: CollectionReference,
    private val auth: FirebaseAuth
) : FirestoreRepository {

    override fun userAvailable() = callbackFlow<Either<String, Boolean>> {
        val userEmail = auth.currentUser?.email.toString()
        collectionRef.whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { document ->
                if (document.size() == 0) {
                    Either.Right(false)
                } else {
                    document.documents.forEach {
                        val doc = it.toObject(UserModel::class.java)
                        if (userEmail == doc?.email.toString()) {
                            Either.Right(true)
                        }
                    }
                }
            }.addOnFailureListener { error ->
                Either.Left(error.toString())
            }
    }.flowOn(Dispatchers.IO).catch {
        Either.Left(it.localizedMessage ?: "Error Occurred!")
    }
}