package com.nafanya.mp3world.presentation.player_view.di

import androidx.media3.common.util.UnstableApi
import com.nafanya.mp3world.presentation.player_view.BottomControlViewFragment
import com.nafanya.mp3world.presentation.player_view.FullscreenControlsFragment
import com.nafanya.mp3world.presentation.player_view.current_playlist.CurrentPlaylistDialogFragment
import dagger.Subcomponent

@Subcomponent(modules = [PlayerViewModule::class])
@UnstableApi
interface PlayerViewComponent {

    fun inject(bottomFragment: BottomControlViewFragment)
    fun inject(fullscreenControlsFragment: FullscreenControlsFragment)
    fun inject(currentPlaylistDialogFragment: CurrentPlaylistDialogFragment)
}
