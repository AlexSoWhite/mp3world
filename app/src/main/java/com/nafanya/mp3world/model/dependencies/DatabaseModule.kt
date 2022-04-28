package com.nafanya.mp3world.model.dependencies

import android.content.Context
import com.nafanya.mp3world.model.localStorage.DatabaseHolder
import com.nafanya.mp3world.model.localStorage.LocalStorageProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideDataBase(@ApplicationContext applicationContext: Context): LocalStorageProvider {
        return LocalStorageProvider(
            DatabaseHolder(applicationContext)
        )
    }
}
