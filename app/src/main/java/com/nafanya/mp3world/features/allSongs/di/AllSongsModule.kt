package com.nafanya.mp3world.features.allSongs.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.viewModel.ViewModelKey
import com.nafanya.mp3world.core.listManagers.ALL_SONGS_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.listManagers.ListManagerKey
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.mp3world.features.allSongs.viewModel.AllSongsViewModel
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
    fun bindListManager(songListManager: SongListManager): ListManager
}
