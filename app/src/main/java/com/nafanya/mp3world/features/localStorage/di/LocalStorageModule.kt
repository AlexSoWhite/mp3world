package com.nafanya.mp3world.features.localStorage.di

import android.content.Context
import com.nafanya.mp3world.features.localStorage.api.AllPlaylistsInteractor
import com.nafanya.mp3world.features.localStorage.api.FavouritesInteractor
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
    ): FavouritesInteractor {
        return localStorageInteractor
    }

    @Provides
    @Singleton
    fun provideAllPlaylistsListProvider(
        localStorageInteractor: LocalStorageInteractor
    ): AllPlaylistsInteractor {
        return localStorageInteractor
    }
}
