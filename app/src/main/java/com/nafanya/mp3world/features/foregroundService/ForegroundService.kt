package com.nafanya.mp3world.features.foregroundService

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.NotificationUtil.IMPORTANCE_HIGH
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.coroutines.AppDispatchers
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.entrypoint.InitialActivity
import com.nafanya.mp3world.core.wrappers.SongImageBitmapFactory
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// TODO customize
class ForegroundService : Service(), CoroutineScope {

    companion object {
        const val CHANNEL_ID = "playback_channel"
    }

    private val coroutineJob = Job()
    override val coroutineContext: CoroutineContext
        get() = AppDispatchers.io + coroutineJob

    /**
     * Notification manager responsible for displaying player notification.
     * Stored as a field to destroy it with the service.
     */
    private lateinit var playerNotificationManager: PlayerNotificationManager

    @Inject
    lateinit var playerInteractor: PlayerInteractor
    private val player: Player
        get() = playerInteractor.player

    @Inject
    lateinit var songImageBitmapFactory: SongImageBitmapFactory

    override fun onCreate() {
        (application as PlayerApplication).applicationComponent
            .foregroundServiceComponent()
            .inject(this)
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                CHANNEL_ID,
                "player",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
            val notification = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("").build()
            startForeground(1, notification)
        }
        playerNotificationManager = PlayerNotificationManager
            .Builder(this, 1, CHANNEL_ID)
            .setChannelImportance(IMPORTANCE_HIGH)
            .setMediaDescriptionAdapter(Adapter(this))
            .setNotificationListener(NotificationListener())
            .setSmallIconResourceId(R.drawable.icv_music_notificatioin)
            .build().apply {
                setPriority(PRIORITY_HIGH)
                setUseFastForwardAction(false)
                setUseRewindAction(false)
                setUseNextActionInCompactView(true)
                setUsePreviousActionInCompactView(true)
                setPlayer(player)
            }
    }

    /**
     * Class responsible for displaying the song data in the notification.
     */
    inner class Adapter(
        private val context: Context
    ) : PlayerNotificationManager.MediaDescriptionAdapter {

        private var previousSong: SongWrapper? = null

        private var previousBitmap: Bitmap? = null

        /**
         * Song title.
         */
        override fun getCurrentContentTitle(player: Player): CharSequence {
            val song = playerInteractor
                .currentPlaylist
                .value
                ?.songList
                ?.get(player.currentMediaItemIndex) as SongWrapper
            return song.title
        }

        /**
         * Song artist.
         */
        override fun getCurrentContentText(player: Player): CharSequence {
            val song = playerInteractor
                .currentPlaylist
                .value
                ?.songList
                ?.get(player.currentMediaItemIndex) as SongWrapper
            return song.artist
        }

        /**
         * Song image.
         */
        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            val song = playerInteractor
                .currentPlaylist
                .value
                ?.songList
                ?.get(player.currentMediaItemIndex) as SongWrapper

            if (previousSong?.equals(song) != true) {
                previousSong = song
                launch {
                    songImageBitmapFactory.getBitmapForSong(song).collectLatest {
                        if (it != null) {
                            previousBitmap = it
                            callback.onBitmap(it)
                        }
                    }
                }
            }

            return previousBitmap
        }

        /**
         * Method triggered when notification clicked.
         */
        @SuppressLint("UnspecifiedImmutableFlag")
        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            val intentToMain = Intent(context, InitialActivity::class.java)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getActivity(
                    context,
                    0,
                    intentToMain,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                PendingIntent.getActivity(
                    context,
                    0,
                    intentToMain,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        }
    }

    /**
     * Support class.
     */
    inner class NotificationListener : PlayerNotificationManager.NotificationListener {

        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            super.onNotificationPosted(notificationId, notification, ongoing)
            this@ForegroundService.startForeground(notificationId, notification)
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            super.onNotificationCancelled(notificationId, dismissedByUser)
            stopSelf()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    /**
     * Destroying service.
     */
    override fun onDestroy() {
        super.onDestroy()
        playerNotificationManager.setPlayer(null)
    }
}
