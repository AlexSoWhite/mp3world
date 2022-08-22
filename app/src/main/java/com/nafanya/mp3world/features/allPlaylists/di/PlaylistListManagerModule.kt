package com.nafanya.mp3world.features.allPlaylists.di

import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.listManagers.ListManagerKey
import com.nafanya.mp3world.core.listManagers.PLAYLIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.features.allPlaylists.PlaylistListManager
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface PlaylistListManagerModule {

    @Binds
    @[IntoMap ListManagerKey(PLAYLIST_LIST_MANAGER_KEY)]
    fun bind(playlistListManager: PlaylistListManager): ListManager
}
