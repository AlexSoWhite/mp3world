package com.nafanya.mp3world.core.di

import android.app.Application
import com.google.gson.Gson
import com.nafanya.player.PlayerInteractor
import okhttp3.OkHttpClient

class PlayerApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .context(this)
            .playerInteractor(PlayerInteractor(this))
            .gson(Gson())
            .okHttpClient(OkHttpClient())
            .build()
    }
}
