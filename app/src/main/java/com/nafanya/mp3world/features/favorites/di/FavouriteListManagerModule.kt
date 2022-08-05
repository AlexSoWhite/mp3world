package com.nafanya.mp3world.features.favorites.di

import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.features.favorites.FavouriteListManager
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
interface FavouriteListManagerModule {

    @Binds
    @IntoSet
    fun bindFavouriteListManager(favouriteListManager: FavouriteListManager): ListManager
}
