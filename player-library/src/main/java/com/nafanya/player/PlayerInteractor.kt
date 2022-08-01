package com.nafanya.player

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.Player

class PlayerInteractor(
    context: Context
) {
    /**
     * Player itself
     */
    private var mPlayer: PlaylistPlayer = PlaylistPlayer(context)
    val player: Player?
        get() = mPlayer.player
    /**
     * Object that connects player state with its LiveData
     */
    private var listener: Listener = Listener(this)
    val currentSong: LiveData<Song>
        get() = listener.currentSong
    val isPlaying: LiveData<Boolean>
        get() = listener.isPlaying
    val currentPlaylist: LiveData<Playlist?>
        get() = mPlayer.currentPlaylist
    private val mIsPlayerInitialised: MutableLiveData<Boolean> = MutableLiveData(false)
    val isPlayerInitialised: LiveData<Boolean>
        get() = mIsPlayerInitialised

    init {
        mPlayer.addListener(listener)
        mIsPlayerInitialised.value = true
    }

    fun setPlaylist(playlist: Playlist) {
        mPlayer.setPlaylist(playlist)
    }

    fun setSong(song: Song) {
        mPlayer.setSong(song)
    }

    fun destroy() {
        mPlayer.player?.removeListener(listener)
        mPlayer.destroy()
        listener.destroy()
    }
}
