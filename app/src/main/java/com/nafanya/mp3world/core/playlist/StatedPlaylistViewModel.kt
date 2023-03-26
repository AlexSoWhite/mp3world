package com.nafanya.mp3world.core.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
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
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

abstract class StatedPlaylistViewModel(
    val playlist: Flow<PlaylistWrapper>,
    final override val baseTitle: String = "",
) : StatedListViewModel<SongWrapper, SongListItem>(),
    PlaylistViewModel,
    TitleViewModel<List<SongWrapper>> {

    @Inject
    lateinit var playerInteractor: PlayerInteractor

    override val mTitle = MutableStateFlow(baseTitle)

    override val stateMapper: (
        suspend (State<List<SongWrapper>>) -> State<List<SongWrapper>>
    )? = null

    private var mIsPlaying = MutableLiveData(false)
    override val isPlaying: LiveData<Boolean>
        get() = mIsPlaying

    final override val isFirstItemBound: MutableLiveData<Boolean> = MutableLiveData(false)

    private val mCurrentSong = MutableLiveData<SongWrapper>()
    final override val currentSong: LiveData<SongWrapper>
        get() = mCurrentSong

    init {
        viewModelScope.launch {
            model.startListeningModelForTitle()
            playerInteractor.isPlaying.collectLatest {
                mIsPlaying.value = it
            }
            combine(
                playerInteractor.currentSong.map { it as SongWrapper }.asFlow(),
                isFirstItemBound.asFlow()
            ) { song, _ ->
                mCurrentSong.value = song
            }.collect()
        }
    }

    override fun onFirstItemBound() {
        isFirstItemBound.value = true
    }

    override fun onSongClick(song: Song) {
        viewModelScope.launch {
            playlist.take(1).collect {
                playerInteractor.setPlaylist(it)
                playerInteractor.setSong(song)
            }
        }
    }
}
