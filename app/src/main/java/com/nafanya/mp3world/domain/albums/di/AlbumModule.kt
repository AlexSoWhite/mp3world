package com.nafanya.mp3world.domain.albums.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.view_model.ViewModelKey
import com.nafanya.mp3world.core.list_managers.ALBUM_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.list_managers.PlaylistProvider
import com.nafanya.mp3world.core.list_managers.ListManagerKey
import com.nafanya.mp3world.domain.albums.AlbumPlaylistProvider
import com.nafanya.mp3world.domain.albums.AlbumPlaylistProviderImpl
import com.nafanya.mp3world.presentation.albums.AlbumListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AlbumModule {

    @Binds
    @[IntoMap ViewModelKey(AlbumListViewModel::class)]
    fun bindViewModel(albumListViewModel: AlbumListViewModel): ViewModel

    @Binds
    @[IntoMap ListManagerKey(ALBUM_LIST_MANAGER_KEY)]
    fun bindListManagerIntoMap(albumListManagerImpl: AlbumPlaylistProviderImpl): PlaylistProvider

    @Binds
    fun bindListManager(albumListManagerImpl: AlbumPlaylistProviderImpl): AlbumPlaylistProvider
}
