package com.nafanya.mp3world.data.local_storage.di

import android.content.Context
import com.nafanya.mp3world.core.coroutines.DispatchersProvider
import com.nafanya.mp3world.data.local_storage.api.UserPlaylistsRepository
import com.nafanya.mp3world.data.local_storage.api.FavouritesRepository
import com.nafanya.mp3world.data.local_storage.LocalStorageRepository
import com.nafanya.mp3world.data.local_storage.LocalStorageRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalStorageModule {

    @Provides
    @Singleton
    fun localStorageRepository(
        context: Context,
        dispatchersProvider: DispatchersProvider
    ): LocalStorageRepository {
        return LocalStorageRepositoryImpl(context, dispatchersProvider)
    }

    @Provides
    @Singleton
    fun provideFavouriteListProvider(
        localStorageInteractor: LocalStorageRepository
    ): FavouritesRepository {
        return localStorageInteractor
    }

    @Provides
    @Singleton
    fun provideAllPlaylistsListProvider(
        localStorageInteractor: LocalStorageRepository
    ): UserPlaylistsRepository {
        return localStorageInteractor
    }
}
