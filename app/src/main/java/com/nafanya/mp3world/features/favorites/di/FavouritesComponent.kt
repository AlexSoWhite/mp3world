package com.nafanya.mp3world.features.favorites.di

import com.nafanya.mp3world.features.favorites.view.FavouriteListActivity
import dagger.Subcomponent

@Subcomponent(modules = [FavouritesModule::class])
interface FavouritesComponent {

    fun inject(favouriteListActivity: FavouriteListActivity)
}
