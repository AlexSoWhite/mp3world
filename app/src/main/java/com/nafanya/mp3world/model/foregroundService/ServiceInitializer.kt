package com.nafanya.mp3world.model.foregroundService

import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song

/**
 * Service that is responsible for starting foreground player service.
 * As the application catches ANR since Android 8 if we don't call startForeground() after few seconds from calling startForegroundService(), it's decided to control service start.
 * Listens SongListManager state.
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
        val observer = Observer<MutableList<Song>> {
            if (!isServiceInitialized && it.isNotEmpty()) {
                PlayerLiveDataProvider.currentPlaylist.value =
                    Playlist(
                        songList = it,
                        id = -1,
                        name = "Мои песни"
                    )
                initService()
                isServiceInitialized = true
            }
        }
        SongListManager.songList.observe(this, observer)
    }

    override fun onBind(p0: Intent): IBinder? {
        super.onBind(p0)
        return null
    }
}
