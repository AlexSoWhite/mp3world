package com.nafanya.player

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer

class PlayerInteractor(
    context: Context
) {
    /**
     * Player itself
     */
    private var _player: PlaylistPlayer = PlaylistPlayer(context)
    val player: ExoPlayer?
        get() = _player.player
    /**
     * Object that connects player state with its LiveData
     */
    private var listener: Listener = Listener(this)
    val currentSong: LiveData<Song>
        get() = listener.currentSong
    val isPlaying: LiveData<Boolean>
        get() = listener.isPlaying
    val currentPlaylist: LiveData<Playlist?>
        get() = _player.currentPlaylist
    private val _isPlayerInitialised: MutableLiveData<Boolean> = MutableLiveData(false)
    val isPlayerInitialised: LiveData<Boolean>
        get() = _isPlayerInitialised

    init {
        _player.addListener(listener)
        _isPlayerInitialised.value = true
    }

    fun setPlaylist(playlist: Playlist) {
        _player.setPlaylist(playlist)
    }

    fun setSong(song: Song) {
        _player.setSong(song)
    }

    fun destroy() {
        _player.player?.removeListener(listener)
        _player.destroy()
        listener.destroy()
    }
}
