package com.nafanya.mp3world.model.foregroundService

import android.annotation.SuppressLint
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
import com.nafanya.mp3world.model.listManagers.StatisticInfoManager
import com.nafanya.mp3world.model.localStorage.LocalStorageProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.model.wrappers.SongStatisticEntity
import com.nafanya.mp3world.view.MainActivity
import java.util.Date

class ForegroundService : LifecycleService() {

    private var player: ExoPlayer? = null
    private var isInitialized = false
    private lateinit var playlist: Playlist
    private lateinit var playerNotificationManager: PlayerNotificationManager
    var currentIdx: Int = 0
    // statistic
    private var startPlayingSongTime: Date? = null
    private var endPlayingSongTime: Date? = null
    private var previousSong: Song? = null

    override fun onCreate() {
        super.onCreate()
        val context = this
        if (player == null) {
            // init player
            player = ExoPlayer.Builder(context).build()
            // start playlist and current song observers
            subscribePlaylist()
            subscribeSong()
            // add listener that trigger observers to sync player state with classes throughout the app
            player!!.addListener(Listener)
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
                .build()
            playerNotificationManager.setUseFastForwardAction(false)
            playerNotificationManager.setUseRewindAction(false)
            playerNotificationManager.setUseNextActionInCompactView(true)
            playerNotificationManager.setUsePreviousActionInCompactView(true)
            playerNotificationManager.setPlayer(player)
            // provide player data to set it into the player controller in the app
            ForegroundServiceLiveDataProvider.setPlayer(player)
        }
    }

    inner class Adapter(private val context: Context) :
        PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return playlist.songList[player.currentMediaItemIndex].title as CharSequence
        }

        @SuppressLint("UnspecifiedImmutableFlag")
        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            val intentToMain = Intent(context, MainActivity::class.java)
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

        override fun getCurrentContentText(player: Player): CharSequence {
            return playlist.songList[player.currentMediaItemIndex].artist as CharSequence
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            return ForegroundServiceLiveDataProvider.currentSong.value!!.art
        }
    }

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

    private fun subscribePlaylist() {
        val observer = Observer<Playlist> {
            // create copy of playlist to continue playing deleted from playlist songs
            val songList = mutableListOf<Song>()
            it?.songList?.forEach { song ->
                songList.add(song)
            }
            val playlist = Playlist(
                id = it.id,
                name = it.name,
                songList = songList
            )
            player?.clearMediaItems()
            this.playlist = playlist
            this.playlist.songList.forEach { song ->
                val extras = Bundle()
                extras.putLong("id", song.id)
                extras.putString("title", song.title)
                extras.putString("artist", song.artist)
                extras.putString("date", song.date)
                extras.putString("url", song.url)
                // TODO extras.putInt("duration", song.duration!!)
                extras.putString("path", song.path)
                val uri: Uri =
                    song.url?.toUri()
                        ?: ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            song.id
                        )
                val mediaItem = MediaItem.Builder()
                    .setUri(uri)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setExtras(extras)
                            .build()
                    ).build()

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
        ForegroundServiceLiveDataProvider.currentPlaylist.observe(this, observer)
    }

    private fun subscribeSong() {
        val observer = Observer<Song> { song ->
            currentIdx = playlist.songList.indexOf(song)
            try {
                logStatistic(song)
                player?.seekToDefaultPosition(currentIdx)
                player?.play()
            } catch (e: IllegalSeekPositionException) {
                Log.d("subscribe", currentIdx.toString())
            }
        }
        ForegroundServiceLiveDataProvider.currentSong.observe(this, observer)
    }

    @Suppress("NestedBlockDepth")
    private fun logStatistic(remember: Song?) {
        endPlayingSongTime = Date()
        startPlayingSongTime?.let {
            var playingTime = endPlayingSongTime!!.time - startPlayingSongTime!!.time
            var entity: SongStatisticEntity? = null
            StatisticInfoManager.info.value!!.forEach { e ->
                if (e.id == previousSong!!.id) {
                    entity = e
                    e.time?.let {
                        playingTime += it
                    }
                }
            }
            if (playingTime >= addingSongToStatisticEntityThreshold) {
                when (entity) {
                    null -> {
                        LocalStorageProvider.addStatisticEntity(
                            this,
                            SongStatisticEntity(
                                previousSong!!.id,
                                playingTime,
                                previousSong!!.title,
                                previousSong!!.artist
                            )
                        )
                    }
                    else -> {
                        LocalStorageProvider.updateStatisticEntity(
                            this,
                            SongStatisticEntity(
                                previousSong!!.id,
                                playingTime,
                                previousSong!!.title,
                                previousSong!!.artist
                            )
                        )
                    }
                }
            }
        }
        previousSong = remember
        startPlayingSongTime = Date()
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        playerNotificationManager.setPlayer(null)
        player?.release()
        logStatistic(null)
        player = null
    }

    companion object {
        // 5 seconds
        private const val addingSongToStatisticEntityThreshold = 5000
    }
}
