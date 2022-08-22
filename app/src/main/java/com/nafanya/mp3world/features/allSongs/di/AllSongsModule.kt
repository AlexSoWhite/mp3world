package com.nafanya.mp3world.features.allSongs.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.ViewModelKey
import com.nafanya.mp3world.features.allSongs.viewModel.AllSongsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AllSongsModule {

    @Binds
    @[IntoMap ViewModelKey(AllSongsViewModel::class)]
    fun bind(allSongsViewModel: AllSongsViewModel): ViewModel
}
