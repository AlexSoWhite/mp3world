package com.nafanya.mp3world.features.foregroundService.di

import com.nafanya.mp3world.features.foregroundService.ForegroundService
import com.nafanya.mp3world.features.foregroundService.ServiceInitializer
import dagger.Subcomponent

@Subcomponent
interface ForegroundServiceComponent {

    fun inject(foregroundService: ForegroundService)
    fun inject(serviceInitializer: ServiceInitializer)
}
