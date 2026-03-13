package com.nafanya.player.interactor

import android.content.Context
import android.util.Log
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.nafanya.player.Playlist
import com.nafanya.player.Song
import com.nafanya.player.aoede_player.AoedePlayer
import com.nafanya.player.aoede_player.MediaItemConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@UnstableApi // everything from media3 is unstable
internal class PlayerInteractorImpl : PlayerInteractor {

    private companion object {
        const val TAG = "_PlayerInteractorImpl"
    }

    private val playerInteractorScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val playbackControlScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var _player: AoedePlayer? = null
    /**
     * Player itself (default part that can be passed to the view).
     */
    override val player: Player? get() = _player?.player

    /**
     * Song to that player is currently playing.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override val currentSong: StateFlow<Song?>
        get() = _isPlayerPresent.flatMapLatest {
            if (it) {
                _player!!.currentSong
            } else {
                MutableStateFlow(null)
            }
        }.stateIn(playerInteractorScope, SharingStarted.Eagerly, null)

    /**
     * Value which represents current playback state.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override val isPlaying: StateFlow<Boolean>
        get() = _isPlayerPresent.flatMapLatest {
            if (it) {
                _player!!.isPlaying
            } else {
                MutableStateFlow(false)
            }
        }.stateIn(playerInteractorScope, SharingStarted.Eagerly, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val currentPlaylist: StateFlow<Playlist?>
        get() = _isPlayerPresent
            .flatMapLatest {
                if (it) {
                    _player!!.currentPlaylist
                } else {
                    MutableStateFlow(null)
                }
            }
            .stateIn(playerInteractorScope, SharingStarted.Eagerly, null)

    private val _isPlayerPresent = MutableStateFlow(false)
    override val isPlayerPresent: StateFlow<Boolean> get() = _isPlayerPresent

    private val _isPlayerReady = MutableStateFlow(false)
    /**
     * [AoedePlayer] considered as initialized when the first song is submitted.
     */
    override val isPlayerReady: StateFlow<Boolean> get() = _isPlayerReady

    init {
        playerInteractorScope.launch {
            combine(_isPlayerPresent, currentSong) { isPlayerPresent, song ->
                _isPlayerReady.value = isPlayerPresent && song != null
            }.collect()
        }
    }

    override fun initializePlayerIfNeeded(context: Context, mediaItemConverter: MediaItemConverter): Player {
        Log.d(TAG, "initializePlayerIfNeeded")
        if (_player == null) {
            Log.d(TAG, "initializePlayerIfNeeded - initializing")
            _player = AoedePlayer(context, mediaItemConverter)
            Log.d(TAG, "initializePlayerIfNeeded, player: $_player, exoPlayer: $player")
            _isPlayerPresent.value = true
        }
        Log.d(TAG, "initializePlayerIfNeeded - player initialized")
        return _player!!.player
    }

    private fun executeWhenPlayerIsPresentAsync(block: () -> Unit) {
        playbackControlScope.launch {
            _isPlayerPresent.filter { it }.first()
            block()
        }
    }

    override fun releasePlayer() {
        Log.d(TAG, "releasingPlayer")
        _player?.player?.release()
        _player = null
        _isPlayerPresent.value = false
    }

    override fun setPlaylist(playlist: Playlist) {
        Log.d(TAG, "setPlaylist")
        executeWhenPlayerIsPresentAsync {
            Log.d(TAG, "setPlaylist - player present")
            _player?.setPlaylist(playlist)
        }
    }

    override fun setSong(song: Song) {
        Log.d(TAG, "setSong")
        executeWhenPlayerIsPresentAsync {
            Log.d(TAG, "setSong - player present")
            _player?.setSong(song)
        }
    }

    override fun suspendPlayer() {
        Log.d(TAG, "suspendPlayer")
        executeWhenPlayerIsPresentAsync {
            Log.d(TAG, "suspend - player present")
            _player?.player?.pause()
        }
    }
}
