package com.nafanya.player

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerInteractor(
    context: Context
) {

    // todo: verify that it should be Job
    private val playerInteractorScope = CoroutineScope(Job() + Dispatchers.IO)

    /**
     * Player itself (extended object with customized logic).
     */
    private val _player = AoedePlayer(context)
    /**
     * Player itself (default part that can be passed to the view).
     */
    val player: Player
        get() = _player.player

    private val _currentSong = MutableSharedFlow<Song>(replay = 1)
    /**
     * Song to that player is currently playing.
     */
    val currentSong: SharedFlow<Song>
        get() = _currentSong

    private val _isPlaying = MutableSharedFlow<Boolean>(replay = 1)
    /**
     * Value which represents current playback state.
     */
    val isPlaying: SharedFlow<Boolean>
        get() = _isPlaying

    /**
     * Object that connects [AoedePlayer] state with its LiveData.
     */
    private var listener: Listener = Listener(this).apply {
        setOnCurrentSongUpdateListener {
            playerInteractorScope.launch {
                _currentSong.emit(it)
            }
        }
        setOnIsPlayingChangeListener {
            playerInteractorScope.launch {
                _isPlaying.emit(it)
            }
        }
    }

    /**
     * Playlist which is currently submitted to the player.
     */
    val currentPlaylist: LiveData<Playlist>
        get() = _player.currentPlaylist

    private val _isFirstSongSubmitted = MutableStateFlow(false)

    /**
     * [AoedePlayer] considered as initialized when the first song is submitted.
     */
    val isFirstSongSubmitted: StateFlow<Boolean>
        get() = _isFirstSongSubmitted

    init {
        _player.addListener(listener)
        playerInteractorScope.launch {
            currentSong.collect {
                if (!isFirstSongSubmitted.value) {
                    _isFirstSongSubmitted.value = true
                }
            }
        }
    }

    fun setPlaylist(playlist: Playlist) {
        _player.setPlaylist(playlist)
    }

    fun setSong(song: Song) {
        _player.setSong(song)
    }

    fun suspendPlayer() {
        _player.suspend()
    }
}
