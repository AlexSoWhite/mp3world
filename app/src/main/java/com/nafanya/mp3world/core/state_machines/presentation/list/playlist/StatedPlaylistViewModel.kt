package com.nafanya.mp3world.core.state_machines.presentation.list.playlist

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.coroutines.collectLatestInScope
import com.nafanya.mp3world.core.coroutines.emitInScope
import com.nafanya.mp3world.core.state_machines.presentation.list.StatedListViewModel
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.presentation.song_list_views.SongListItem
import com.nafanya.player.PlayerInteractor
import com.nafanya.player.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("LongLogTag")
abstract class StatedPlaylistViewModel : StatedListViewModel<SongWrapper, SongListItem>() {

    private companion object {
        const val TAG = "_StatedPlaylistViewModel"
    }

    abstract val playlistFlow: Flow<PlaylistWrapper>

    abstract val playerInteractor: PlayerInteractor

    private var _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> get() = _isPlaying.asStateFlow()

    private val _currentSong = MutableSharedFlow<SongWrapper>(replay = 1)
    val currentSong: SharedFlow<SongWrapper>
        get() = _currentSong

    fun init() {
        Log.d(TAG, "init called")
        playerInteractor.isPlaying.collectLatestInScope(viewModelScope) { _isPlaying.value = it }
        playerInteractor.currentSong.collectLatestInScope(viewModelScope) {
            Log.d(TAG, "current song updated: $it")
            _currentSong.emitInScope(viewModelScope, it as SongWrapper)
        }
    }

    // todo: toggle song playing
    fun onSongClick(song: Song) {
        viewModelScope.launch {
            val playlistWrapper = playlistFlow.first()
            playerInteractor.setPlaylist(playlistWrapper)
            playerInteractor.setSong(song)
        }
    }
}
