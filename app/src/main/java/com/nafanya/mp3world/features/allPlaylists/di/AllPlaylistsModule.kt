package com.nafanya.mp3world.features.allPlaylists.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.ViewModelKey
import com.nafanya.mp3world.features.allPlaylists.viewModel.AllPlaylistsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AllPlaylistsModule {

    @Binds
    @[IntoMap ViewModelKey(AllPlaylistsViewModel::class)]
    fun providesPlaylistListViewModel(allPlaylistsViewModel: AllPlaylistsViewModel): ViewModel
}
