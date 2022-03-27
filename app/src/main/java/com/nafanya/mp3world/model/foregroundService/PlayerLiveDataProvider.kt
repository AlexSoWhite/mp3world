package com.nafanya.mp3world.model.foregroundService

import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song

object PlayerLiveDataProvider {

    val currentPlaylist: MutableLiveData<Playlist> by lazy {
        MutableLiveData<Playlist>()
    }

    val currentSong: MutableLiveData<Song> by lazy {
        MutableLiveData<Song>()
    }

    val isPlayerInitialized: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val isPlaying: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    private var player: ExoPlayer? = null

    fun setPlayer(player: ExoPlayer?) {
        PlayerLiveDataProvider.player = player
        isPlayerInitialized.value = true
    }

    fun getPlayer(): ExoPlayer? {
        return player
    }
}
