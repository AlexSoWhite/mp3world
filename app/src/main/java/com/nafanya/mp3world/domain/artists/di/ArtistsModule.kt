package com.nafanya.mp3world.domain.artists.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.view_model.ViewModelKey
import com.nafanya.mp3world.core.list_managers.ARTIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.list_managers.PlaylistProvider
import com.nafanya.mp3world.core.list_managers.ListManagerKey
import com.nafanya.mp3world.domain.artists.ArtistPlaylistProvider
import com.nafanya.mp3world.domain.artists.ArtistPlaylistProviderImpl
import com.nafanya.mp3world.presentation.artists.ArtistListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface ArtistsModule {

    @Binds
    @[IntoMap ViewModelKey(ArtistListViewModel::class)]
    fun bindViewModel(artistListViewModel: ArtistListViewModel): ViewModel

    @Binds
    @Singleton
    @[IntoMap ListManagerKey(ARTIST_LIST_MANAGER_KEY)]
    fun bindIntoMap(artistListManager: ArtistPlaylistProviderImpl): PlaylistProvider

    @Binds
    @Singleton
    fun bind(artistListManager: ArtistPlaylistProviderImpl): ArtistPlaylistProvider
}
