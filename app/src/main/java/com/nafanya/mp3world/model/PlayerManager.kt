package com.nafanya.mp3world.model

import android.content.ContentUris
import android.content.Context
import android.media.MediaPlayer
import android.provider.MediaStore

object PlayerManager {

    private var mediaPlayer = MediaPlayer()
    private var isPlaying = false

    fun play(context: Context, song: Song) {
        if (isPlaying) {
            mediaPlayer.reset()
            isPlaying = false
        }
        isPlaying = true
        mediaPlayer.setDataSource(
            context,
            ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.id)
        )
        mediaPlayer.prepare()
        mediaPlayer.start()
    }
}
