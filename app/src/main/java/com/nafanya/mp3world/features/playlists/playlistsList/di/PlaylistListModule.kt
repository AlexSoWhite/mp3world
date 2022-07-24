package com.nafanya.mp3world.features.playlists.playlistsList.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.ViewModelKey
import com.nafanya.mp3world.features.playlists.playlistsList.viewModel.PlaylistListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface PlaylistListModule {

    @Binds
    @[IntoMap ViewModelKey(PlaylistListViewModel::class)]
    fun providesPlaylistListViewModel(playlistListViewModel: PlaylistListViewModel): ViewModel
}
