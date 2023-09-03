package com.nafanya.mp3world.features.favourites.di

import com.nafanya.mp3world.features.favourites.view.FavouritesActivity
import dagger.Subcomponent

@Subcomponent
interface FavouritesComponent {

    fun inject(favouritesActivity: FavouritesActivity)
}

interface FavouritesComponentProvider {

    val favouritesComponent: FavouritesComponent
}
