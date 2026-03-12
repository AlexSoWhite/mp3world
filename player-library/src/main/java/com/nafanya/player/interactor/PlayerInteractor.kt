package com.nafanya.player.interactor

import android.content.Context
import androidx.media3.common.Player
import com.nafanya.player.Playlist
import com.nafanya.player.Song
import kotlinx.coroutines.flow.StateFlow

interface PlayerInteractor {
    val player: Player?
    val currentSong: StateFlow<Song?>
    val isPlaying: StateFlow<Boolean>
    val currentPlaylist: StateFlow<Playlist?>
    val isPlayerPresent: StateFlow<Boolean>
    val isPlayerReady: StateFlow<Boolean>

    fun initializePlayerIfNeeded(context: Context): Player
    fun releasePlayer()
    fun setPlaylist(playlist: Playlist)
    fun setSong(song: Song)
    fun suspendPlayer()
}

class PlayerInteractorFactory {
    fun createPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()
    }
}
