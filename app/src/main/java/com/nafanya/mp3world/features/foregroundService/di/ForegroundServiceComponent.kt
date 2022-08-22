package com.nafanya.mp3world.features.foregroundService.di

import com.nafanya.mp3world.features.foregroundService.ForegroundService
import dagger.Subcomponent

@Subcomponent
interface ForegroundServiceComponent {

    fun inject(foregroundService: ForegroundService)
}
