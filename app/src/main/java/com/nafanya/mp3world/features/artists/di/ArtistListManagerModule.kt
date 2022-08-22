package com.nafanya.mp3world.features.artists.di

import com.nafanya.mp3world.core.listManagers.ARTIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.listManagers.ListManagerKey
import com.nafanya.mp3world.features.artists.ArtistListManager
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ArtistListManagerModule {

    @Binds
    @[IntoMap ListManagerKey(ARTIST_LIST_MANAGER_KEY)]
    fun bind(artistListManager: ArtistListManager): ListManager
}
