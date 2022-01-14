package com.nafanya.mp3world.model

import android.content.ContentUris
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.nafanya.mp3world.viewmodel.ForegroundServiceLiveDataHolder

class ForegroundService : LifecycleService() {

    private lateinit var player: ExoPlayer
    private lateinit var playlist: Playlist
    private lateinit var playerNotificationManager: PlayerNotificationManager
    var currentIdx: Int = 0

    override fun onCreate() {
        super.onCreate()
        val context = this
        player = ExoPlayer.Builder(context).build()
        subscribePlaylist()
        subscribeSong()
        player.addListener(Listener)
        playerNotificationManager = PlayerNotificationManager.Builder(
            context,
            5,
            ""
        ).build()
        playerNotificationManager.setPlayer(player)
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
                player.addMediaItem(
                    mediaItem
                )
            }
            player.prepare()
        }
        ForegroundServiceLiveDataHolder.currentPlaylist.observe(this, observer)
    }

    fun subscribeSong() {
        val observer = Observer<Song> {
            currentIdx = playlist.songList.indexOf(it)
            player.seekToDefaultPosition(currentIdx)
            player.play()
        }
        ForegroundServiceLiveDataHolder.currentSong.observe(this, observer)
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }
}
