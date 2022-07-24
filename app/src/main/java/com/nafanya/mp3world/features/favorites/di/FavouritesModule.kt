package com.nafanya.mp3world.features.favorites.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.ViewModelKey
import com.nafanya.mp3world.features.allSongs.SongListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface FavouritesModule {

    @Binds
    @[IntoMap ViewModelKey(SongListViewModel::class)]
    fun provideSongListViewModel(songListViewModel: SongListViewModel): ViewModel
}
