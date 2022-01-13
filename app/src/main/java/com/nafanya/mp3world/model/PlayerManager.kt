package com.nafanya.mp3world.model

import android.content.ContentUris
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.ui.StyledPlayerControlView

object PlayerManager {

    private lateinit var player: ExoPlayer
    private lateinit var playlist: Playlist
    var currentIdx: Int = 0
    lateinit var selectedSong: Song

    fun initPlayer(context: Context, playerView: StyledPlayerControlView) {
        player = ExoPlayer.Builder(context).build()
        player.addListener(Listener)
        playerView.player = player
    }

    fun setPlaylist(playlist: Playlist) {
        this.playlist = playlist
        playlist.songList.forEach {
            val extras = Bundle()
            extras.putLong("id", it.id)
            val mediaItem = MediaItem.Builder()
                .setUri(
                    ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        it.id
                    )
                ).setMediaMetadata(
                    MediaMetadata.Builder()
                        .setExtras(extras)
                        .setTitle(it.title as CharSequence)
                        .setArtist(it.artist as CharSequence)
                        .build()
                ).build()
            player.addMediaItem(
                mediaItem
            )
        }
        player.prepare()
    }

    fun moveToSong(song: Song) {
        currentIdx = playlist.songList.indexOf(song)
        selectedSong = song
        player.seekToDefaultPosition(currentIdx)
    }

    fun play() {
        player.play()
    }
}
