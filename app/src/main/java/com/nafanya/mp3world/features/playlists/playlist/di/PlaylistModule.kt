package com.nafanya.mp3world.features.playlists.playlist.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.ViewModelKey
import com.nafanya.mp3world.features.allSongs.SongListViewModel
import com.nafanya.mp3world.features.playlists.playlist.viewModel.AddSongToListViewModel
import com.nafanya.mp3world.features.playlists.playlist.viewModel.PlaylistViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface PlaylistModule {

    @Binds
    @[IntoMap ViewModelKey(PlaylistViewModel::class)]
    fun providePlaylistViewModel(playlistViewModel: PlaylistViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(SongListViewModel::class)]
    fun provideSongListViewModel(songListViewModel: SongListViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(AddSongToListViewModel::class)]
    fun provideAddSongToListViewModel(addSongToListViewModel: AddSongToListViewModel): ViewModel
}
