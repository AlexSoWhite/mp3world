package com.nafanya.mp3world.model

import android.content.ContentUris
import android.content.Context
import android.media.MediaPlayer
import android.provider.MediaStore
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

object PlayerManager {

//    private var mediaPlayer = MediaPlayer()
    private var isPlaying = false
    private lateinit var exoPlayer: ExoPlayer

//    fun play(context: Context, song: Song, songList: ArrayList<Song>) {
//        if (isPlaying) {
//            mediaPlayer.reset()
//            isPlaying = false
//        }
//        isPlaying = true
//        var idx = songList.indexOf(song)
//        var currSong = songList[idx]
//        mediaPlayer.setDataSource(
//            context,
//            ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong.id)
//        )
//        mediaPlayer.prepare()
//        mediaPlayer.start()
//        mediaPlayer.setOnCompletionListener {
//            idx++
//            currSong = songList[idx]
//            it.reset()
//            it.setDataSource(
//                context,
//                ContentUris.withAppendedId(
//                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                    currSong.id
//                )
//            )
//            it.prepare()
//            it.start()
//        }
//    }

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
