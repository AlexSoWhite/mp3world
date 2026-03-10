package com.nafanya.mp3world.features.favourites.di

import com.nafanya.mp3world.core.list_managers.FAVOURITE_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.list_managers.PlaylistProvider
import com.nafanya.mp3world.core.list_managers.ListManagerKey
import com.nafanya.mp3world.features.favourites.domain.FavouritesProvider
import com.nafanya.mp3world.features.favourites.domain.FavouritesProviderImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface FavouritesModule {

    @Binds
    @[IntoMap ListManagerKey(FAVOURITE_LIST_MANAGER_KEY)]
    fun bindIntoMap(favouritesManagerImpl: FavouritesProviderImpl): PlaylistProvider

    @Binds
    fun bind(favouritesManagerImpl: FavouritesProviderImpl): FavouritesProvider
}
