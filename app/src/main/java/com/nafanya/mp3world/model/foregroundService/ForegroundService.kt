package com.nafanya.mp3world.model.foregroundService

import android.app.Notification
import android.app.PendingIntent
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.IllegalSeekPositionException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.NotificationUtil.IMPORTANCE_DEFAULT
import com.google.android.exoplayer2.util.NotificationUtil.createNotificationChannel
import com.nafanya.mp3world.R
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.view.InitialActivity
import kotlinx.coroutines.DelicateCoroutinesApi

class ForegroundService : LifecycleService() {

    /**
     * Player itself
     */
    private var player: ExoPlayer? = null

    /**
     * Indicator fot preventing playing as the application opens
     */
    private var isInitialized = false

    /**
     * Current playlist
     */
    private lateinit var playlist: Playlist

    /**
     * Notification manager responsible for displaying player notification.
     * Stored as a field to destroy it with the service.
     */
    private lateinit var playerNotificationManager: PlayerNotificationManager

    /**
     * Object that connects player state with its LiveData
     */
    private lateinit var listener: Listener

    /**
     * Field for navigating through playlist
     */
    var currentIdx: Int = 0

    override fun onCreate() {
        super.onCreate()
        val context = this
        if (player == null) {
            // init player
            player = ExoPlayer.Builder(context).build()
            // init playlist and current song observers
            subscribePlaylist()
            subscribeSong()
            // add listener that trigger observers to sync player state with other classes throughout the app
            listener = Listener()
            player!!.addListener(listener)
            // add headphones unplugging handler
            player!!.setHandleAudioBecomingNoisy(true)
            // add audio focus change handler
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build()
            player!!.setAudioAttributes(audioAttributes, true)
            // create player notification
            createNotificationChannel(
                this,
                "playback_channel",
                R.string.name,
                R.string.description,
                IMPORTANCE_DEFAULT
            )
            playerNotificationManager = PlayerNotificationManager
                .Builder(this, 1, "playback_channel")
                .setChannelImportance(IMPORTANCE_DEFAULT)
                .setMediaDescriptionAdapter(Adapter(this))
                .setNotificationListener(NotificationListener())
                .setSmallIconResourceId(R.drawable.music_notification_icon)
                .build()
            playerNotificationManager.setUseFastForwardAction(false)
            playerNotificationManager.setUseRewindAction(false)
            playerNotificationManager.setUseNextActionInCompactView(true)
            playerNotificationManager.setUsePreviousActionInCompactView(true)
            playerNotificationManager.setPlayer(player)
            // provide player data to set it into the player controller in the app
            PlayerLiveDataProvider.setPlayer(player)
        }
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
            return playlist.songList[player.currentMediaItemIndex].title as CharSequence
        }

        /**
         * Song artist.
         */
        override fun getCurrentContentText(player: Player): CharSequence {
            return playlist.songList[player.currentMediaItemIndex].artist as CharSequence
        }

        /**
         * Song image.
         */
        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            return PlayerLiveDataProvider.currentSong.value!!.art
        }

        /**
         * Method triggered when notification clicked.
         */
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

    /**
     * Method that creates playlist observer.
     * TODO: modifying playlist at runtime
     * TODO: start playing when there are no songs in the local storage
     */
    private fun subscribePlaylist() {
        val observer = Observer<Playlist> {
            // creating copy of playlist to continue playing deleted from playlist songs
            val songList = mutableListOf<Song>()
            it?.songList?.forEach { song ->
                songList.add(song)
            }
            val playlist = Playlist(
                id = it.id,
                name = it.name,
                songList = songList
            )
            // clearing player
            player?.clearMediaItems()
            // setting new playlist
            this.playlist = playlist
            this.playlist.songList.forEach { song ->
                // creating bundle for listener to provide correct metadata
                val extras = Bundle()
                extras.putLong("id", song.id)
                extras.putString("title", song.title)
                extras.putString("artist", song.artist)
                song.date?.let { it1 -> extras.putLong("date", it1) }
                extras.putString("url", song.url)
                extras.putLong("duration", song.duration!!)
                extras.putString("artUrl", song.artUrl)
                // extracting uri: remote song (url) or song from the local MediaStore
                val uri: Uri =
                    song.url?.toUri()
                        ?: ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            song.id
                        )
                // creating mediaItem to play and to provide data
                val mediaItem = MediaItem.Builder()
                    .setUri(uri)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setExtras(extras)
                            .build()
                    ).build()
                // adding song to player
                player?.addMediaItem(
                    mediaItem
                )
            }
            if (isInitialized) {
                player?.prepare()
            } else {
                isInitialized = true
            }
        }
        PlayerLiveDataProvider.currentPlaylist.observe(this, observer)
    }

    /**
     * Method that creates song observer.
     * Expects that song exists in the current playlist.
     */
    private fun subscribeSong() {
        val observer = Observer<Song> { song ->
            currentIdx = playlist.songList.indexOf(song)
            try {
                player?.seekToDefaultPosition(currentIdx)
                player?.play()
            } catch (e: IllegalSeekPositionException) {
                Log.d("subscribe", currentIdx.toString())
            }
        }
        PlayerLiveDataProvider.currentSong.observe(this, observer)
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
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
        player?.release()
        player = null
        listener.destroy()
    }
}
