package com.nafanya.mp3world.features.artists.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.ViewModelKey
import com.nafanya.mp3world.features.artists.viewModel.ArtistListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ArtistsModule {

    @Binds
    @[IntoMap ViewModelKey(ArtistListViewModel::class)]
    fun provideArtistsViewModel(artistListViewModel: ArtistListViewModel): ViewModel
}
