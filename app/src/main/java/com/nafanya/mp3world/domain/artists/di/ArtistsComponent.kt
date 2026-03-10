package com.nafanya.mp3world.domain.artists.di

import com.nafanya.mp3world.presentation.artists.ArtistListFragment
import dagger.Subcomponent

@Subcomponent
interface ArtistsComponent {

    fun inject(artistsFragment: ArtistListFragment)
}

interface ArtistsComponentProvider {

    val artistsComponent: ArtistsComponent
}
