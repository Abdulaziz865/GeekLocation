package com.example.geeklocation.di

import com.example.data.repositories.LocationRepositoryImpl
import com.example.domain.repositories.LocationRepository
import com.example.geeklocation.Constants
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideLocationsRef() = Firebase.firestore.collection(Constants.LOCATIONS)

    @Provides
    fun provideLocationsRepository(locationsRef: CollectionReference): LocationRepository = LocationRepositoryImpl(locationsRef)
}