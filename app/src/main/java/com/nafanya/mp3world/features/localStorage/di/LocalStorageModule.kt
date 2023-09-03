package com.nafanya.mp3world.features.localStorage.di

import android.content.Context
import com.nafanya.mp3world.features.localStorage.AllPlaylistsListInteractor
import com.nafanya.mp3world.features.localStorage.FavouriteListInteractor
import com.nafanya.mp3world.features.localStorage.LocalStorageInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalStorageModule {

    @Provides
    @Singleton
    fun localStorageInteractor(
        context: Context
    ): LocalStorageInteractor {
        return LocalStorageInteractor(context)
    }

    @Provides
    @Singleton
    fun provideFavouriteListProvider(
        localStorageInteractor: LocalStorageInteractor
    ): FavouriteListInteractor {
        return localStorageInteractor
    }

    @Provides
    @Singleton
    fun provideAllPlaylistsListProvider(
        localStorageInteractor: LocalStorageInteractor
    ): AllPlaylistsListInteractor {
        return localStorageInteractor
    }
}
