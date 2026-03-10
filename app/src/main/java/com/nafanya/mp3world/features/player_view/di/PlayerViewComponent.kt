package com.nafanya.mp3world.features.player_view.di

import com.nafanya.mp3world.features.player_view.presentation.BottomControlViewFragment
import com.nafanya.mp3world.features.player_view.presentation.FullscreenControlsFragment
import com.nafanya.mp3world.features.player_view.presentation.current_playlist.CurrentPlaylistDialogFragment
import dagger.Subcomponent

@Subcomponent(modules = [PlayerViewModule::class])
interface PlayerViewComponent {

    fun inject(bottomFragment: BottomControlViewFragment)
    fun inject(fullscreenControlsFragment: FullscreenControlsFragment)
    fun inject(currentPlaylistDialogFragment: CurrentPlaylistDialogFragment)
}
