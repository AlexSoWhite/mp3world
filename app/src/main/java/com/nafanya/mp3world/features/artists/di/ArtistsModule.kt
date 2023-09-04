package com.nafanya.mp3world.features.artists.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.viewModel.ViewModelKey
import com.nafanya.mp3world.core.listManagers.ARTIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.listManagers.ListManagerKey
import com.nafanya.mp3world.features.artists.ArtistListManager
import com.nafanya.mp3world.features.artists.ArtistListManagerImpl
import com.nafanya.mp3world.features.artists.viewModel.ArtistListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ArtistsModule {

    @Binds
    @[IntoMap ViewModelKey(ArtistListViewModel::class)]
    fun bindViewModel(artistListViewModel: ArtistListViewModel): ViewModel

    @Binds
    @[IntoMap ListManagerKey(ARTIST_LIST_MANAGER_KEY)]
    fun bindIntoMap(artistListManager: ArtistListManagerImpl): ListManager

    @Binds
    fun bind(artistListManager: ArtistListManagerImpl): ArtistListManager
}
