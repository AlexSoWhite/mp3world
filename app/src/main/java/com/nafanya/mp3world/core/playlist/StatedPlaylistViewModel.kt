package com.nafanya.mp3world.core.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.coroutines.collectInScope
import com.nafanya.mp3world.core.coroutines.collectLatestInScope
import com.nafanya.mp3world.core.stateMachines.commonUi.list.StatedListViewModel
import com.nafanya.mp3world.core.viewModel.PlaylistViewModel
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.player.PlayerInteractor
import com.nafanya.player.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

abstract class StatedPlaylistViewModel :
    StatedListViewModel<SongWrapper, SongListItem>(),
    PlaylistViewModel {

    abstract val playlistFlow: Flow<PlaylistWrapper>

    val mIsInteractorBound = MutableStateFlow(false)
    lateinit var playerInteractor: PlayerInteractor

    private var pendingSong: Song? = null

    private var mIsPlaying = MutableLiveData(false)
    override val isPlaying: LiveData<Boolean>
        get() = mIsPlaying

    private val mCurrentSong = MutableLiveData<SongWrapper>()
    final override val currentSong: LiveData<SongWrapper>
        get() = mCurrentSong

    init {
        viewModelScope.launch {
            mIsInteractorBound.collectLatest {
                if (it) {
                    pendingSong?.let { song -> onSongClick(song) }
                }
            }
        }
    }

    override fun onSongClick(song: Song) {
        viewModelScope.launch {
            mIsInteractorBound.collectLatest {
                if (it) {
                    playlistFlow.take(1).collectLatest { playlistWrapper ->
                        playerInteractor.setPlaylist(playlistWrapper)
                        playerInteractor.setSong(song)
                    }
                } else {
                    pendingSong = song
                }
            }
        }
    }

    fun bindInteractor(interactor: PlayerInteractor) {
        playerInteractor = interactor
        playerInteractor.isPlaying.collectInScope(viewModelScope, mIsPlaying::setValue)
        playerInteractor.currentSong.collectLatestInScope(viewModelScope) {
            mCurrentSong.value = it as SongWrapper
        }
        mIsInteractorBound.value = true
    }
}
