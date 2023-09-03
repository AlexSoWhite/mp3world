package com.nafanya.mp3world.features.allPlaylists.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.ViewModelKey
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.listManagers.ListManagerKey
import com.nafanya.mp3world.core.listManagers.PLAYLIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.features.allPlaylists.PlaylistListManager
import com.nafanya.mp3world.features.allPlaylists.allPlaylists.AllPlaylistsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AllPlaylistsModule {

    @Binds
    @[IntoMap ViewModelKey(AllPlaylistsViewModel::class)]
    fun allPlaylistsViewModel(allPlaylistsViewModel: AllPlaylistsViewModel): ViewModel

    @Binds
    @[IntoMap ListManagerKey(PLAYLIST_LIST_MANAGER_KEY)]
    fun playlistListManager(playlistListManager: PlaylistListManager): ListManager
}
