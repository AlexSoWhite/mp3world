package com.nafanya.mp3world.features.favorites.di

import com.nafanya.mp3world.features.favorites.view.FavouritesActivity
import dagger.Subcomponent

@Subcomponent
interface FavouritesComponent {

    fun inject(favouritesActivity: FavouritesActivity)
}

interface FavouritesComponentProvider {

    val favouritesComponent: FavouritesComponent
}
