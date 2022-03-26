package com.nafanya.mp3world.model.foregroundService

import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.nafanya.mp3world.model.wrappers.Playlist

class ServiceInitializer : LifecycleService() {

    private var isServiceInitialized = false

    private fun initService() {
        val intent = Intent(this.applicationContext, ForegroundService::class.java)
        startForegroundService(this.applicationContext, intent)
    }

    override fun onCreate() {
        super.onCreate()
        val observer = Observer<Playlist> {
            if (!isServiceInitialized && it.songList.isNotEmpty()) {
                initService()
                isServiceInitialized = true
            }
        }
        PlayerLiveDataProvider.currentPlaylist.observe(this, observer)
    }

    override fun onBind(p0: Intent): IBinder? {
        super.onBind(p0)
        return null
    }
}
