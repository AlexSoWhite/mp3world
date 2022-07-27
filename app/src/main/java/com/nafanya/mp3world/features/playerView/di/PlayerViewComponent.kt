package com.nafanya.mp3world.features.playerView.di

import com.nafanya.mp3world.features.playerView.view.BottomControlView
import com.nafanya.mp3world.features.playerView.view.FullscreenControlsFragment
import dagger.Subcomponent

@Subcomponent(modules = [PlayerViewModule::class])
interface PlayerViewComponent {

    fun inject(bottomFragment: BottomControlView)
    fun inject(fullscreenControlsFragment: FullscreenControlsFragment)
}
