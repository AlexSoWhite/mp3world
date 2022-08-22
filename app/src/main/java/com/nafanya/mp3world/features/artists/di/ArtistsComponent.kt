package com.nafanya.mp3world.features.artists.di

import com.nafanya.mp3world.features.artists.view.ArtistListFragment
import dagger.Subcomponent

@Subcomponent(modules = [ArtistsModule::class])
interface ArtistsComponent {

    fun inject(artistsFragment: ArtistListFragment)
}
