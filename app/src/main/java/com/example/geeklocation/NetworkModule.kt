package com.example.geeklocation

import android.content.SharedPreferences
import com.example.data.repositories.AuthorizeAuthorizeSharedPreferencesRepositoryImpl
import com.example.domain.repositories.AuthorizeSharedPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRepository(sharedPreferences: SharedPreferences) : AuthorizeSharedPreferencesRepository {
        return AuthorizeAuthorizeSharedPreferencesRepositoryImpl(sharedPreferences)
    }
}