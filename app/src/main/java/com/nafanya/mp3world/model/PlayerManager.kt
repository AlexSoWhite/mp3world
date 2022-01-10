package com.nafanya.mp3world.model

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

object PlayerManager {

    private lateinit var player: ExoPlayer
    private lateinit var playlist: Playlist

    fun initPlayer(context: Context) {
        player = ExoPlayer.Builder(context).build()
    }

    fun setPlaylist(playlist: Playlist) {
        this.playlist = playlist
        playlist.songList.forEach {
            player.addMediaItem(
                MediaItem.fromUri(
                    ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        it.id
                    )
                )
            )
        }
        player.prepare()
    }

    fun moveToSong(song: Song) {
        val idx = playlist.songList.indexOf(song)
        player.seekToDefaultPosition(idx)
    }

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }

}
