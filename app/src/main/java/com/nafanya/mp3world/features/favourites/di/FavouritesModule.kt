package com.nafanya.mp3world.features.favourites.di

import com.nafanya.mp3world.core.listManagers.FAVOURITE_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.listManagers.ListManagerKey
import com.nafanya.mp3world.features.favourites.FavouritesManager
import com.nafanya.mp3world.features.favourites.FavouritesManagerImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface FavouritesModule {

    @Binds
    @[IntoMap ListManagerKey(FAVOURITE_LIST_MANAGER_KEY)]
    fun bindIntoMap(favouritesManagerImpl: FavouritesManagerImpl): ListManager

    @Binds
    fun bind(favouritesManagerImpl: FavouritesManagerImpl): FavouritesManager
}
