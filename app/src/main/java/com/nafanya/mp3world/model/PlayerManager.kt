package com.nafanya.mp3world.model

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

object PlayerManager {

    private var isPlaying = false
    private lateinit var exoPlayer: ExoPlayer

    fun initPlayer(context: Context) {
        exoPlayer = ExoPlayer.Builder(context).build()
    }

    fun play(song: Song, songList: ArrayList<Song>) {
        val mediaItem = MediaItem.fromUri(
            ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                song.id
            )
        )
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun pause() {
        exoPlayer.pause()
    }

    fun resume() {
        exoPlayer.play()
    }
}
