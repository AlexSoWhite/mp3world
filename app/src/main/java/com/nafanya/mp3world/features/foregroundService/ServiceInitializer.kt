package com.nafanya.mp3world.features.foregroundService

import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.player.PlayerInteractor
import com.nafanya.player.Playlist
import com.nafanya.player.Song
import javax.inject.Inject

/**
 * Service that is responsible for starting foreground player service.
 * As the application catches ANR since Android 8 if we don't call startForeground() after few seconds
 * from calling startForegroundService(),
 * it's decided to control service start.
 * Listens SongListManager state.
 */
class ServiceInitializer : LifecycleService() {

    @Inject
    lateinit var playerInteractor: PlayerInteractor

    private var isServiceInitialized = false

    private fun initService() {
        val intent = Intent(this.applicationContext, ForegroundService::class.java)
        startForegroundService(this.applicationContext, intent)
        stopSelf()
    }

    override fun onCreate() {
        (application as PlayerApplication)
            .applicationComponent
            .foregroundServiceComponent()
            .inject(this)
        super.onCreate()
        val observerLocal = Observer<MutableList<Song>> {
            if (!isServiceInitialized && it.isNotEmpty()) {
                playerInteractor.setPlaylist(
                    Playlist(
                        songList = it,
                        id = -1,
                        name = "Мои песни"
                    )
                )
                initService()
                isServiceInitialized = true
            }
        }
        SongListManager.songList.observe(this, observerLocal)
        val observerRemote = Observer<Playlist?> {
            if (!isServiceInitialized && it.songList.isNotEmpty()) {
                initService()
                isServiceInitialized = true
            }
        }
        playerInteractor.currentPlaylist.observe(this, observerRemote)
    }

    override fun onBind(p0: Intent): IBinder? {
        super.onBind(p0)
        return null
    }
}
