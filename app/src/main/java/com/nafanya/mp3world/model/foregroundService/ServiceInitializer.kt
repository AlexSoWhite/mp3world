package com.nafanya.mp3world.model.foregroundService

import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.nafanya.mp3world.model.wrappers.Playlist

/**
 * Service responsible for starting foreground player service.
 * As the application catches ANR since Android 8 if we don't call startForeground() after few seconds from calling startForegroundService(), it's better to control service start.
 */
class ServiceInitializer : LifecycleService() {

    private var isServiceInitialized = false

    private fun initService() {
        val intent = Intent(this.applicationContext, ForegroundService::class.java)
        startForegroundService(this.applicationContext, intent)
        stopSelf()
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
