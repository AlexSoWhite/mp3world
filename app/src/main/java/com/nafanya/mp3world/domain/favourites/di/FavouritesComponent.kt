package com.nafanya.mp3world.domain.favourites.di

import com.nafanya.mp3world.presentation.favourites.FavouritesActivity
import dagger.Subcomponent

@Subcomponent
interface FavouritesComponent {

    fun inject(favouritesActivity: FavouritesActivity)
}

interface FavouritesComponentProvider {

    val favouritesComponent: FavouritesComponent
}
