package com.nafanya.mp3world.features.foregroundService

import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.nafanya.mp3world.core.domain.Song
import com.nafanya.mp3world.features.foregroundService.PlayerLiveDataProvider.currentPlaylist
import com.nafanya.mp3world.features.foregroundService.PlayerLiveDataProvider.currentSong
import com.nafanya.mp3world.features.foregroundService.PlayerLiveDataProvider.isPlayerInitialized
import com.nafanya.mp3world.features.foregroundService.PlayerLiveDataProvider.isPlaying
import com.nafanya.mp3world.features.foregroundService.PlayerLiveDataProvider.player
import com.nafanya.mp3world.features.playlists.playlist.Playlist

/**
 * Object that holds player data.
 * @property currentPlaylist holds current playlist, updates from app. Player resets its playlist as this field updated.
 * @property currentSong holds current song, updates from app. Player resets its song as this field updated.
 * @property isPlayerInitialized holds player initialization state, updates by player.
 * @property isPlaying holds playing state, updates by player.
 * @property player holds player itself. Is not wrapped with LiveData to prevent null pointer issues.
 */
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
