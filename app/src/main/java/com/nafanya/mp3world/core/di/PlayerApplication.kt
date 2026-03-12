package com.nafanya.mp3world.core.di

import android.app.Application
import com.google.gson.Gson
import com.nafanya.mp3world.core.coroutines.DispatchersProvider
import com.nafanya.player.interactor.PlayerInteractorFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient

class PlayerApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .context(this)
            .playerInteractor(PlayerInteractorFactory().createPlayerInteractor())
            .gson(Gson())
            .okHttpClient(OkHttpClient())
            .dispatchersProvider(DispatchersProvider())
            .applicationScope(applicationScope)
            .build()
    }
}
