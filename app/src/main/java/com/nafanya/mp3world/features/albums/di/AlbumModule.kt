package com.nafanya.mp3world.features.albums.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.ViewModelKey
import com.nafanya.mp3world.features.albums.viewModel.AlbumListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AlbumModule {

    @Binds
    @[IntoMap ViewModelKey(AlbumListViewModel::class)]
    fun providesAlbumListViewModel(albumListViewModel: AlbumListViewModel): ViewModel
}
