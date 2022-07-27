package com.nafanya.mp3world.features.playerView.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.ViewModelKey
import com.nafanya.mp3world.features.playerView.view.PlayerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface PlayerViewModule {

    @Binds
    @[IntoMap ViewModelKey(PlayerViewModel::class)]
    fun playerViewModel(playerViewModel: PlayerViewModel): ViewModel
}
