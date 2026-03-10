package com.nafanya.mp3world.features.all_songs.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.view_model.ViewModelKey
import com.nafanya.mp3world.core.list_managers.ALL_SONGS_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.list_managers.PlaylistProvider
import com.nafanya.mp3world.core.list_managers.ListManagerKey
import com.nafanya.mp3world.features.all_songs.domain.SongPlaylistProvider
import com.nafanya.mp3world.features.all_songs.domain.SongPlaylistProviderImpl
import com.nafanya.mp3world.features.all_songs.presentation.AllSongsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AllSongsModule {

    @Binds
    @[IntoMap ViewModelKey(AllSongsViewModel::class)]
    fun bindViewModel(allSongsViewModel: AllSongsViewModel): ViewModel

    @Binds
    @[IntoMap ListManagerKey(ALL_SONGS_LIST_MANAGER_KEY)]
    fun bindIntoMap(songListManager: SongPlaylistProviderImpl): PlaylistProvider

    @Binds
    fun bind(songListManager: SongPlaylistProviderImpl): SongPlaylistProvider
}
