package com.nafanya.mp3world.core.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.stateMachines.State
import com.nafanya.mp3world.core.stateMachines.list.StatedListViewModel
import com.nafanya.mp3world.core.stateMachines.title.TitleViewModel
import com.nafanya.mp3world.core.viewModel.PlaylistViewModel
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.player.PlayerInteractor
import com.nafanya.player.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

abstract class StatedPlaylistViewModel(
    private val playerInteractor: PlayerInteractor,
    val playlist: Flow<PlaylistWrapper>,
    final override val baseTitle: String = "",
) : StatedListViewModel<SongWrapper, SongListItem>(),
    PlaylistViewModel,
    TitleViewModel<List<SongWrapper>> {

    override val mTitle = MutableStateFlow(baseTitle)

    override val stateMapper: (
        suspend (State<List<SongWrapper>>) -> State<List<SongWrapper>>
    )? = null

    init {
        viewModelScope.launch {
            model.startListeningModelForTitle()
        }
    }

    override val isPlaying: LiveData<Boolean> = playerInteractor.isPlaying
    final override val isFirstItemBound: MutableLiveData<Boolean> = MutableLiveData(false)

    final override val currentSong = combine(
        playerInteractor.currentSong.map { it as SongWrapper }.asFlow(),
        isFirstItemBound.asFlow()
    ) { song, _ ->
        song
    }.asLiveData()

    override fun onFirstItemBound() {
        isFirstItemBound.value = true
    }

    override fun onSongClick(song: Song) {
        viewModelScope.launch {
            playlist.collect {
                playerInteractor.setPlaylist(it)
                playerInteractor.setSong(song)
            }
        }
    }
}
