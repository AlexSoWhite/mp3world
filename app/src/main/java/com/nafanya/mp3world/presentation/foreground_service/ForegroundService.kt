package com.nafanya.mp3world.presentation.foreground_service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.bumptech.glide.Glide
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.presentation.entrypoint.InitialActivity
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.wrappers.song.toSongWrapper
import com.nafanya.mp3world.presentation.core.images.SongImageBitmapFactory
import com.nafanya.mp3world.presentation.core.images.glide.CustomBitmapTarget
import com.nafanya.player.Song
import com.nafanya.player.aoede_player.MediaItemConverter
import com.nafanya.player.interactor.PlayerInteractor
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@UnstableApi // everything from media3 is unstable
class ForegroundService : MediaSessionService() {

    companion object {
        private const val CHANNEL_ID = "playback_channel"
        private const val CHANNEL_NAME = "player"
        private const val NOTIFICATION_ID = 1
        private const val TAG = "_ForegroundService"
    }

    /**
     * Notification manager responsible for displaying player notification.
     * Stored as a field to destroy it with the service.
     */
    private var playerNotificationManager: PlayerNotificationManager? = null

    @Inject
    lateinit var playerInteractor: PlayerInteractor

    @Inject
    lateinit var songImageBitmapFactory: SongImageBitmapFactory

    private var mediaSession: MediaSession? = null

    private val foregroundServiceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        (application as PlayerApplication).applicationComponent
            .foregroundServiceComponent()
            .inject(this)

        super.onCreate()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
        val defaultNotification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(this.getString(R.string.foreground_idle_title))
            .build()
        startForeground(NOTIFICATION_ID, defaultNotification)

        val player = playerInteractor.initializePlayerIfNeeded(
            context = this,
            mediaItemConverter = object : MediaItemConverter {
                override fun getSongFromMediaItem(mediaItem: MediaItem): Song? {
                    return mediaItem.toSongWrapper()
                }
            }
        )
        mediaSession = MediaSession.Builder(this, player).build()

        foregroundServiceScope.launch {
            playerInteractor.isPlayerReady.filter { it }.first()
            playerNotificationManager = PlayerNotificationManager
                .Builder(this@ForegroundService, NOTIFICATION_ID, CHANNEL_ID)
                .setMediaDescriptionAdapter(Adapter(this@ForegroundService))
                .setNotificationListener(NotificationListener())
                .setSmallIconResourceId(R.drawable.icv_music_notificatioin)
                .build().apply {
                    setPriority(PRIORITY_HIGH)
                    setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
                    setColorized(true)
                    setUseFastForwardAction(false)
                    setUseRewindAction(false)
                    setUseNextActionInCompactView(true)
                    setUsePreviousActionInCompactView(true)
                    setPlayer(player)
                }
        }
    }

    /**
     * Class responsible for displaying the song data in the notification.
     */
    inner class Adapter(
        private val context: Context
    ) : PlayerNotificationManager.MediaDescriptionAdapter {

        private val TAG = ForegroundService.TAG + "Adapter"

        private var bitmapCache = mutableMapOf<Song, Bitmap?>()

        /**
         * Song title.
         */
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return player.currentMediaItem?.mediaMetadata?.title ?: ""
        }

        /**
         * Song artist.
         */
        override fun getCurrentContentText(player: Player): CharSequence {
            return player.currentMediaItem?.mediaMetadata?.artist ?: ""
        }

        /**
         * Song image.
         */
        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            Log.d(TAG, "contentLargeIcon requested")
            val song = player.currentMediaItem?.toSongWrapper() ?: return null
            Log.d(TAG, "song obtained from media item: $song")
            if (!bitmapCache.containsKey(song)) {
                Log.d(TAG, "new song, obtaining image")
                foregroundServiceScope.launch {
                    Glide.with(this@ForegroundService)
                        .asBitmap()
                        .load(song)
                        .into(
                            CustomBitmapTarget(
                                onLoaded = {
                                    Log.d(TAG, "bitmap for song obtained")
                                    bitmapCache[song] = it
                                    callback.onBitmap(it)
                                },
                            )
                        )
                }
            } else {
                Log.d(TAG, "cached song, returning cached bitmap straight away")
            }
            return bitmapCache[song]
        }

        /**
         * Method triggered when notification clicked.
         */
        @SuppressLint("UnspecifiedImmutableFlag")
        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            val intentToMain = Intent(context, InitialActivity::class.java)
            return PendingIntent.getActivity(
                context,
                0,
                intentToMain,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
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
            Log.d(TAG, "onNotificationCancelled")
            super.onNotificationCancelled(notificationId, dismissedByUser)
            stopSelf()
        }
    }

    override fun onLowMemory() {
        Log.d(TAG, "onLowMemory")
        super.onLowMemory()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    /**
     * Destroying service.
     */
    override fun onDestroy() {
        playerNotificationManager?.setPlayer(null)
        playerNotificationManager = null
        mediaSession?.run {
            playerInteractor.releasePlayer()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession
}
