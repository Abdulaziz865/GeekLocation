package com.example.geeklocation.di

import com.example.data.repositories.AuthorizeRepositoryImpl
import com.example.domain.repositories.AuthorizeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun authorizeRepository(authorizeRepositoryImpl: AuthorizeRepositoryImpl): AuthorizeRepository

}