package com.nafanya.mp3world.features.artists.di

import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.features.artists.ArtistListManager
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
interface ArtistListManagerModule {

    @Binds
    @IntoSet
    fun bindArtistListManager(artistListManager: ArtistListManager): ListManager
}
