package com.nafanya.mp3world.features.artists.di

import com.nafanya.mp3world.features.artists.view.ArtistListActivity
import dagger.Subcomponent

@Subcomponent(modules = [ArtistsModule::class])
interface ArtistsComponent {

    fun inject(artistListActivity: ArtistListActivity)
}
