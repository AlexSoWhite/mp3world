package com.nafanya.mp3world.model

import android.app.Notification
import android.app.PendingIntent
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.IllegalSeekPositionException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.NotificationUtil.IMPORTANCE_HIGH
import com.google.android.exoplayer2.util.NotificationUtil.createNotificationChannel
import com.nafanya.mp3world.R
import com.nafanya.mp3world.view.MainActivity
import com.nafanya.mp3world.viewmodel.ForegroundServiceLiveDataProvider

class ForegroundService : LifecycleService() {

    private var player: ExoPlayer? = null
    private lateinit var playlist: Playlist
    private lateinit var playerNotificationManager: PlayerNotificationManager
    var currentIdx: Int = 0

    override fun onCreate() {
        super.onCreate()
        val context = this
        player = ExoPlayer.Builder(context).build()
        subscribePlaylist()
        subscribeSong()
        player?.addListener(Listener)
        createNotificationChannel(
            this,
            "playback_channel",
            R.string.name,
            R.string.description,
            IMPORTANCE_HIGH
        )
        playerNotificationManager = PlayerNotificationManager
            .Builder(this, 1, "playback_channel")
            .setChannelImportance(IMPORTANCE_HIGH)
            .setMediaDescriptionAdapter(Adapter(this))
            .setNotificationListener(NotificationListener())
            .build()
        playerNotificationManager.setPlayer(player)
        ForegroundServiceLiveDataProvider.setPlayer(player)
    }

    inner class Adapter(private val context: Context) :
        PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return playlist.songList[player.currentMediaItemIndex].title as CharSequence
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            val intentToMain = Intent(context, MainActivity::class.java)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getActivity(
                    context,
                    0,
                    intentToMain,
                    PendingIntent.FLAG_UPDATE_CURRENT
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

        override fun getCurrentContentText(player: Player): CharSequence? {
            return playlist.songList[player.currentMediaItemIndex].artist as CharSequence
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            return null
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
            this.playlist = it
            playlist.songList.forEach { song ->
                val extras = Bundle()
                extras.putLong("id", song.id)
                val mediaItem = MediaItem.Builder()
                    .setUri(
                        ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            song.id
                        )
                    ).setMediaMetadata(
                        MediaMetadata.Builder()
                            .setExtras(extras)
                            .setTitle(song.title as CharSequence)
                            .setArtist(song.artist as CharSequence)
                            .build()
                    ).build()
                player?.addMediaItem(
                    mediaItem
                )
            }
            player?.prepare()
        }
        ForegroundServiceLiveDataProvider.currentPlaylist.observe(this, observer)
    }

    private fun subscribeSong() {
        val observer = Observer<Song> {
            currentIdx = playlist.songList.indexOf(it)
            try {
                player?.seekToDefaultPosition(currentIdx)
                player?.play()
            } catch (e: IllegalSeekPositionException) {
                Log.d("subscribe", currentIdx.toString())
            }
        }
        ForegroundServiceLiveDataProvider.currentSong.observe(this, observer)
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
        player = null
    }
}
