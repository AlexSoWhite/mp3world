package com.nafanya.mp3world.features.player_view.di

import androidx.lifecycle.ViewModel
import com.nafanya.mp3world.core.di.view_model.ViewModelKey
import com.nafanya.mp3world.features.player_view.presentation.PlayerViewModel
import com.nafanya.mp3world.features.player_view.presentation.current_playlist.CurrentPlaylistViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface PlayerViewModule {

    @Binds
    @[IntoMap ViewModelKey(PlayerViewModel::class)]
    fun playerViewModel(playerViewModel: PlayerViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(CurrentPlaylistViewModel::class)]
    fun currentPlaylistViewModel(currentPlaylistViewModel: CurrentPlaylistViewModel): ViewModel
}
