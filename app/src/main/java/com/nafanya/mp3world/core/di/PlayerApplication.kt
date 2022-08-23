package com.nafanya.mp3world.core.di

import android.app.Application
import com.nafanya.player.PlayerInteractor

class PlayerApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .context(this)
            .playerInteractor(PlayerInteractor(this))
            .build()
    }
}
