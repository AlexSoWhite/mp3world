package com.nafanya.mp3world.features.playlists.playlistsList.di

import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.features.playlists.playlistsList.PlaylistListManager
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
interface PlaylistManagerModule {

    @Binds
    @IntoSet
    fun bindPlaylistListManager(playlistListManager: PlaylistListManager): ListManager
}
