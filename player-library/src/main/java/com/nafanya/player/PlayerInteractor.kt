package com.nafanya.player

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayerInteractor(
    context: Context
) {

    /**
     * Player itself (extended object with customized logic).
     */
    private val mPlayer = AoedePlayer(context)

    /**
     * Player itself (default part that can be passed to the view).
     */
    val player: Player
        get() = mPlayer.player

    /**
     * Object that connects [AoedePlayer] state with its LiveData.
     */
    private var listener: Listener = Listener(this)

    /**
     * Song to that player is currently seek.
     */
    val currentSong: LiveData<Song>
        get() = listener.currentSong

    /**
     * Value which represents current playback state.
     */
    val isPlaying: LiveData<Boolean>
        get() = listener.isPlaying

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

    private val currentSongObserver = Observer<Song> {
        if (!isPlayerInitialised.value) {
            mIsPlayerInitialized.value = true
        }
    }

    init {
        mPlayer.addListener(listener)
        currentSong.observeForever(currentSongObserver)
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
