package com.nafanya.mp3world.model

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerControlView

object PlayerManager {

    private lateinit var player: ExoPlayer
    private lateinit var playlist: Playlist
    var currentIdx: Int = 0

    fun initPlayer(context: Context, playerView: StyledPlayerControlView) {
        player = ExoPlayer.Builder(context).build()
        player.addListener(Listener)
        playerView.player = player
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

    fun getSong(): Song {
        if (currentIdx == playlist.songList.size) {
            currentIdx = 0
        }
        return playlist.songList[currentIdx]
    }

    fun moveToSong(song: Song) {
        currentIdx = playlist.songList.indexOf(song)
        player.seekToDefaultPosition(currentIdx)
    }

    fun play() {
        player.play()
    }
}
