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

    private val playerInteractorScope = CoroutineScope(Job() + Dispatchers.IO)

    /**
     * Player itself (extended object with customized logic).
     */
    private val mPlayer = AoedePlayer(context)

    /**
     * Player itself (default part that can be passed to the view).
     */
    val player: Player
        get() = mPlayer.player

    private val mCurrentSong = MutableSharedFlow<Song>(replay = 1)

    /**
     * Song to that player is currently seek.
     */
    val currentSong: SharedFlow<Song>
        get() = mCurrentSong

    private val mIsPlaying = MutableSharedFlow<Boolean>(replay = 1)

    /**
     * Value which represents current playback state.
     */
    val isPlaying: SharedFlow<Boolean>
        get() = mIsPlaying

    /**
     * Object that connects [AoedePlayer] state with its LiveData.
     */
    private var listener: Listener = Listener(this).apply {
        setOnCurrentSongUpdateListener {
            playerInteractorScope.launch {
                mCurrentSong.emit(it)
            }
        }
        setOnIsPlayingChangeListener {
            playerInteractorScope.launch {
                mIsPlaying.emit(it)
            }
        }
    }

    /**
     * Playlist which is currently submitted to the player.
     */
    val currentPlaylist: LiveData<Playlist>
        get() = mPlayer.currentPlaylist

    private val mIsPlayerInitialized = MutableStateFlow(false)

    /**
     * [AoedePlayer] considered as initialized when the first song is submitted.
     */
    val isPlayerInitialised: StateFlow<Boolean>
        get() = mIsPlayerInitialized

    init {
        mPlayer.addListener(listener)
        playerInteractorScope.launch {
            currentSong.collect {
                if (!isPlayerInitialised.value) {
                    mIsPlayerInitialized.value = true
                }
            }
        }
    }

    fun setPlaylist(playlist: Playlist) {
        mPlayer.setPlaylist(playlist)
    }

    fun setSong(song: Song) {
        mPlayer.setSong(song)
    }

    fun suspendPlayer() {
        mPlayer.suspend()
    }
}
