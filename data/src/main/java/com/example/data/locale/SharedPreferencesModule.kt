package com.example.data.locale

import android.content.Context
import android.content.SharedPreferences
import com.example.data.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Singleton
    @Provides
    fun providesSharedPreferencesInitialize(@ApplicationContext context : Context): SharedPreferences{
        return context.getSharedPreferences(context.getString(R.string.authorize_preference) , Context.MODE_PRIVATE)
    }
}