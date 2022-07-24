package com.nafanya.mp3world.features.searching.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.ViewModelKey
import com.nafanya.mp3world.features.searching.viewModel.SearchSongListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SearchSongsModule {

    @Binds
    @[IntoMap ViewModelKey(SearchSongListViewModel::class)]
    fun providesSearchSongListViewModel(searchSongListViewModel: SearchSongListViewModel): ViewModel
}
