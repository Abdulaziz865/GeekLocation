package com.example.data.repositories

import com.example.domain.repositories.LocationRepository
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val locationRef: CollectionReference
): LocationRepository{
//
//    override fun getLocationsFromFirestore(): Flow<Either<FirebaseFirestoreException , List<LocationModel>>> = callbackFlow {
//        val snapshotListener = locationRef.addSnapshotListener{value, error ->
//            val locationResponse = if (value != null){
//                val locations = value.toObjects(LocationModel::class.java)
//                Either.Right(locations)
//            }
//            else {
//                Either.Left(error)
//            }
//            trySend(locationResponse)
//        }
//        awaitClose{
//            snapshotListener.remove()
//        }
//    }
//
//    override suspend fun addLocationToFirestore(lat: Double, lng: Double): Either<String, Boolean> {
//        return try {
//            val email = locationRef.document().id
//            val location = LocationModel(
//                email = email,
//                latitude = lat,
//                longitude = lng
//            )
//            locationRef.document(email).set(location).await()
//            Either.Right(true)
//        } catch (e: Exception){
//            Either.Left(e.toString())
//        }
//    }
}