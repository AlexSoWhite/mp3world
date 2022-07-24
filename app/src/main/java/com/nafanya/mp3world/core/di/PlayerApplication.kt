package com.nafanya.mp3world.core.di

import android.app.Application

/**
 * Class that provides access to the application context. (TODO: update comment)
 */
class PlayerApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .context(this)
            .build()
    }
}
