package com.nafanya.mp3world.features.player.di

import com.nafanya.mp3world.features.player.view.FullScreenPlayerActivity
import dagger.Subcomponent

@Subcomponent
interface FullScreenPlayerComponent {

    fun inject(fullScreenPlayerActivity: FullScreenPlayerActivity)
}
