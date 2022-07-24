package com.nafanya.mp3world.features.localStorage.di

import android.content.Context
import com.nafanya.mp3world.features.localStorage.DatabaseHolder
import com.nafanya.mp3world.features.localStorage.LocalStorageProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(applicationContext: Context): LocalStorageProvider {
        return LocalStorageProvider(
            DatabaseHolder(applicationContext)
        )
    }
}
