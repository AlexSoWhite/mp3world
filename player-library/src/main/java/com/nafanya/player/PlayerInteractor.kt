package com.nafanya.player

import android.content.Context
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PlayerInteractor(
    context: Context
) {

    private val playerInteractorScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /**
     * Player itself (extended object with customized logic).
     */
    private val _player = AoedePlayer(context)
    /**
     * Player itself (default part that can be passed to the view).
     */
    val player: Player
        get() = _player.player

    private val _currentSong = MutableStateFlow<Song?>(null)
    /**
     * Song to that player is currently playing.
     */
    val currentSong: StateFlow<Song?>
        get() = _currentSong

    private val _isPlaying = MutableStateFlow(false)
    /**
     * Value which represents current playback state.
     */
    val isPlaying: StateFlow<Boolean>
        get() = _isPlaying

    /**
     * Object that connects [AoedePlayer] with public flows.
     */
    private var listener: Listener = Listener(this).apply {
        setOnCurrentSongUpdateListener {
            _currentSong.value = it
        }
        setOnIsPlayingChangeListener {
            _isPlaying.value = it
        }
    }

    /**
     * Playlist which is currently submitted to the player.
     */
    val currentPlaylist: StateFlow<Playlist?>
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
            currentSong.collectLatest {
                _isFirstSongSubmitted.value = it != null
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
