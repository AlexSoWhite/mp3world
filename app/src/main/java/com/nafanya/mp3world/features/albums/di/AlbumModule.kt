package com.nafanya.mp3world.features.albums.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.viewModel.ViewModelKey
import com.nafanya.mp3world.core.listManagers.ALBUM_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.listManagers.ListManagerKey
import com.nafanya.mp3world.features.albums.domain.AlbumListManager
import com.nafanya.mp3world.features.albums.domain.AlbumListManagerImpl
import com.nafanya.mp3world.features.albums.presentation.AlbumListViewModel
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
    fun bindListManagerIntoMap(albumListManagerImpl: AlbumListManagerImpl): ListManager

    @Binds
    fun bindListManager(albumListManagerImpl: AlbumListManagerImpl): AlbumListManager
}
