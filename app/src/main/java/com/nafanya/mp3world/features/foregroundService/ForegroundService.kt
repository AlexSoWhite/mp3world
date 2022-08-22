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
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.NotificationUtil.IMPORTANCE_DEFAULT
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.entrypoint.InitialActivity
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject

// TODO use custom service
class ForegroundService : Service() {

    /**
     * Notification manager responsible for displaying player notification.
     * Stored as a field to destroy it with the service.
     */
    private lateinit var playerNotificationManager: PlayerNotificationManager

    @Inject
    lateinit var playerInteractor: PlayerInteractor
    private val player: Player?
        get() = playerInteractor.player

    override fun onCreate() {
        (application as PlayerApplication).applicationComponent
            .foregroundServiceComponent()
            .inject(this)
        super.onCreate()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "playback_channel",
                "player",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        playerNotificationManager = PlayerNotificationManager
            .Builder(this, 1, "playback_channel")
            .setChannelImportance(IMPORTANCE_DEFAULT)
            .setMediaDescriptionAdapter(Adapter(this)) // TODO bind with app
            .setNotificationListener(NotificationListener())
            .setSmallIconResourceId(R.drawable.music_notification_icon)
            .build()
        playerNotificationManager.setUseFastForwardAction(false)
        playerNotificationManager.setUseRewindAction(false)
        playerNotificationManager.setUseNextActionInCompactView(true)
        playerNotificationManager.setUsePreviousActionInCompactView(true)
        playerNotificationManager.setPlayer(player)
    }

    /**
     * Class responsible for displaying the song data in the notification.
     */
    inner class Adapter(
        private val context: Context
    ) : PlayerNotificationManager.MediaDescriptionAdapter {

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
        ): Bitmap {
            return (playerInteractor.currentSong.value!! as SongWrapper).art
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
            startForeground(notificationId, notification)
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
        playerInteractor.destroy()
    }
}
