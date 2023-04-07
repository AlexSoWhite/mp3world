package com.nafanya.mp3world.features.favorites.di

import com.nafanya.mp3world.core.listManagers.FAVOURITE_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.listManagers.ListManagerKey
import com.nafanya.mp3world.features.favorites.FavouriteListManager
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface FavouriteListManagerModule {

    @Binds
    @[IntoMap ListManagerKey(FAVOURITE_LIST_MANAGER_KEY)]
    fun bind(favouriteListManager: FavouriteListManager): ListManager
}
