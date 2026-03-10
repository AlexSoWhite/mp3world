package com.nafanya.mp3world.presentation.foreground_service.di

import com.nafanya.mp3world.presentation.foreground_service.ForegroundService
import dagger.Subcomponent

@Subcomponent
interface ForegroundServiceComponent {

    fun inject(foregroundService: ForegroundService)
}
