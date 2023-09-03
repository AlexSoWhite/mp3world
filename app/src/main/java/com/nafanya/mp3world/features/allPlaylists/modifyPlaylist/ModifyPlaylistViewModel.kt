package com.nafanya.mp3world.features.allPlaylists.modifyPlaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.coroutines.collectLatestInScope
import com.nafanya.mp3world.core.listManagers.ALL_SONGS_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.ListManagerProvider
import com.nafanya.mp3world.core.listManagers.PLAYLIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.listUtils.searching.SearchProcessor
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.core.listUtils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.listUtils.title.TitleViewModel
import com.nafanya.mp3world.core.listUtils.title.convertStateToTitle
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.stateMachines.State
import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.allPlaylists.PlaylistListManager
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.mp3world.features.allSongs.asAllSongsPlaylist
import com.nafanya.mp3world.features.songListViews.SONG_ADDABLE_REMOVABLE
import com.nafanya.mp3world.features.songListViews.SongListItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ModifyPlaylistViewModel(
    private val playlistListManager: PlaylistListManager,
    private val playlistName: String,
    playlistId: Long,
    songListManager: SongListManager
) : StatedPlaylistViewModel(),
    Searchable<SongWrapper>,
    TitleViewModel<List<SongWrapper>> {

    override val playlistFlow = songListManager.songList.map { it.asAllSongsPlaylist() }.asFlow()

    private val mModifyingPlaylist = MutableLiveData<PlaylistWrapper>()
    val modifyingPlaylist: LiveData<PlaylistWrapper>
        get() = mModifyingPlaylist

    private val songList = mutableListOf<SongWrapper>()

    private val searchProcessor = SearchProcessor(QueryFilter(songQueryFilterCallback))

    private val mTitle = MutableLiveData(playlistName)
    override val title: LiveData<String>
        get() = mTitle

    init {
        model.load {
            searchProcessor.setup(
                this@ModifyPlaylistViewModel,
                songListManager.songList.map {
                    Data.Success(it.asAllSongsPlaylist().songList)
                }.asFlow()
            )
            model.currentState.collectLatestInScope(viewModelScope, ::proceedState)
            viewModelScope.launch {
                playlistListManager.getPlaylistByContainerId(playlistId).asFlow().collectLatest {
                    if (it != null) {
                        mModifyingPlaylist.postValue(it)
                        songList.clear()
                        songList.addAll(it.songList)
                    }
                }
            }
        }
    }

    override fun asListItems(list: List<SongWrapper>): List<SongListItem> {
        return list.map {
            SongListItem(SONG_ADDABLE_REMOVABLE, it)
        }
    }

    override fun search(query: String) {
        searchProcessor.search(query)
    }

    private fun proceedState(state: State<List<SongWrapper>>) {
        val newState = when (state) {
            is State.Success, is State.Updated -> State.Success(songList)
            else -> state
        }
        mTitle.value = convertStateToTitle(newState, playlistName)
    }

    fun confirmChanges() {
        val oldPlaylist = modifyingPlaylist.value!!
        viewModelScope.launch {
            playlistListManager.updatePlaylist(
                oldPlaylist.copy(songList = songList)
            )
        }
    }

    fun addSongToCopyOfPlaylist(song: SongWrapper) {
        songList.add(0, song)
        proceedState(model.currentState.value)
    }

    fun removeSongFromCopyOfPlaylist(song: SongWrapper) {
        songList.remove(song)
        proceedState(model.currentState.value)
    }

    class Factory @AssistedInject constructor(
        @Assisted("playlistId") private val playlistId: Long,
        @Assisted("playlistName") private val playlistName: String,
        private val listManagerProvider: ListManagerProvider
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(playlistId > -1)
            val playlistListManager = listManagerProvider.getListManager(
                PLAYLIST_LIST_MANAGER_KEY
            ) as PlaylistListManager
            val songListManager = listManagerProvider.getListManager(
                ALL_SONGS_LIST_MANAGER_KEY
            ) as SongListManager
            return ModifyPlaylistViewModel(
                playlistListManager,
                playlistName,
                playlistId,
                songListManager
            ) as T
        }

        @AssistedFactory
        interface ModifyPlaylistFactory {

            fun create(
                @Assisted("playlistId") playlistId: Long,
                @Assisted("playlistName") playlistName: String
            ): Factory
        }
    }
}
