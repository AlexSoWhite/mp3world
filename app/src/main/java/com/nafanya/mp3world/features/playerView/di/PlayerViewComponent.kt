package com.nafanya.mp3world.features.playerView.di

import com.nafanya.mp3world.features.playerView.view.BottomControlView
import com.nafanya.mp3world.features.playerView.view.FullScreenPlayerActivity
import dagger.Subcomponent

@Subcomponent(modules = [PlayerViewModule::class])
interface PlayerViewComponent {

    fun inject(fullScreenPlayerActivity: FullScreenPlayerActivity)
    fun inject(bottomFragment: BottomControlView)
}
