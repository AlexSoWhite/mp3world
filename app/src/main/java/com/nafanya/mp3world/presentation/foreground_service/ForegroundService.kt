package com.nafanya.mp3world.presentation.foreground_service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.util.Size
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.media3.common.Player
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.presentation.entrypoint.InitialActivity
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.utils.ColorExtractor
import com.nafanya.mp3world.core.wrappers.song.toSong
import com.nafanya.mp3world.presentation.core.images.SongImageBitmapFactory
import com.nafanya.player.interactor.PlayerInteractor
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// TODO customize
class ForegroundService : MediaSessionService() {

    companion object {
        private const val CHANNEL_ID = "playback_channel"
        private const val TAG = "_ForegroundService"
    }

    /**
     * Notification manager responsible for displaying player notification.
     * Stored as a field to destroy it with the service.
     */
    private lateinit var playerNotificationManager: PlayerNotificationManager

    @Inject
    lateinit var playerInteractor: PlayerInteractor

    @Inject
    lateinit var songImageBitmapFactory: SongImageBitmapFactory

    @Inject
    lateinit var colorExtractor: ColorExtractor

    // todo: it should be released and live alongside player (as intended by android developers)
    private var mediaSession: MediaSession? = null

    private val foregroundServiceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var songImageBimapObtainingJob: Job? = null

    override fun onCreate() {
        Log.d(TAG, "onCreate")
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
        }

        val player = playerInteractor.initializePlayerIfNeeded(this)
        mediaSession = MediaSession.Builder(this, player).build()

        foregroundServiceScope.launch {
            playerInteractor.isPlayerReady.filter { it }.first()
            playerNotificationManager = PlayerNotificationManager
                .Builder(this@ForegroundService, 1, CHANNEL_ID)
                .setMediaDescriptionAdapter(Adapter(this@ForegroundService))
                .setNotificationListener(NotificationListener())
//                .setSmallIconResourceId(R.drawable.icv_music_notificatioin)
                .build().apply {
                    setPriority(PRIORITY_HIGH)
                    setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
                    setColorized(true)
                    setSmallIcon(R.drawable.icv_music_notificatioin)
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

        /**
         * Song title.
         */
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return player.currentMediaItem?.mediaMetadata?.title ?:""
        }

        /**
         * Song artist.
         */
        override fun getCurrentContentText(player: Player): CharSequence {
            return player.currentMediaItem?.mediaMetadata?.artist ?:""
        }

        override fun getCurrentSubText(player: Player): CharSequence? {
            return "mp3world"
        }

        /**
         * Song image.
         *
         * todo: cache
         */
        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            Log.d(TAG, "contentLargeIcon requested")
            foregroundServiceScope.launch {
                Log.d(TAG, "song image obtaining started")
                val song = player.currentMediaItem?.toSong()
                Log.d(TAG, "song obtained from media item: $song")
                if (song != null) {
                    val bitmap = songImageBitmapFactory.getBitmapForSong(song)
                    Log.d(TAG, "bitmap for song obtained")
                    callback.onBitmap(bitmap)
                }
            }
            return null
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
            Log.d(TAG, "onNotificationPosted, notification: $notification")
            super.onNotificationPosted(notificationId, notification, ongoing)
            this@ForegroundService.startForeground(notificationId, notification)
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            Log.d(TAG, "onNotificationCancelled")
            super.onNotificationCancelled(notificationId, dismissedByUser)
            stopSelf()
        }
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
        playerNotificationManager.setPlayer(null)
        mediaSession?.run {
            playerInteractor.releasePlayer()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession
}
