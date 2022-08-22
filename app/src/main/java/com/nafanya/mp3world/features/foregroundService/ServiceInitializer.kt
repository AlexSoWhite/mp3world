package com.nafanya.mp3world.features.foregroundService

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat.startForegroundService
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject

/**
 * Service that is responsible for starting foreground player service.
 * As the application catches ANR since Android 8 if we don't call startForeground() after few seconds
 * from calling startForegroundService(),
 * it's decided to control service start.
 * Listens SongListManager state.
 */
// TODO move initialization away from services
class ServiceInitializer : Service() {

    @Inject
    lateinit var playerInteractor: PlayerInteractor

    @Inject
    lateinit var songListManager: SongListManager

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
        songListManager.songList.observeForever {
            if (!isServiceInitialized && it.isNotEmpty()) {
                playerInteractor.setPlaylist(
                    PlaylistWrapper(
                        songList = it as MutableList<SongWrapper>,
                        id = -1,
                        name = "Мои песни"
                    )
                )
                initService()
                isServiceInitialized = true
            }
        }
        playerInteractor.currentPlaylist.observeForever {
            if (!isServiceInitialized && it?.songList?.isNotEmpty() == true) {
                initService()
                isServiceInitialized = true
            }
        }
    }

    override fun onBind(p0: Intent): IBinder? {
        return null
    }
}
