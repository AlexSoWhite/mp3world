package com.nafanya.mp3world.presentation.favourites.di

import com.nafanya.mp3world.core.list_managers.FAVOURITE_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.list_managers.PlaylistProvider
import com.nafanya.mp3world.core.list_managers.ListManagerKey
import com.nafanya.mp3world.domain.favourites.FavouritesProvider
import com.nafanya.mp3world.domain.favourites.FavouritesProviderImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface FavouritesModule {

    @Binds
    @Singleton
    @[IntoMap ListManagerKey(FAVOURITE_LIST_MANAGER_KEY)]
    fun bindIntoMap(favouritesManagerImpl: FavouritesProviderImpl): PlaylistProvider

    @Binds
    @Singleton
    fun bind(favouritesManagerImpl: FavouritesProviderImpl): FavouritesProvider
}
