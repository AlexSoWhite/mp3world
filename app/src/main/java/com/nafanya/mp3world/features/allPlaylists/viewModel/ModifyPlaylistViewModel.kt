package com.nafanya.mp3world.features.allPlaylists.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.listManagers.ALL_SONGS_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.ListManagerProvider
import com.nafanya.mp3world.core.listManagers.PLAYLIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listUtils.searching.SearchProcessor
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.listUtils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.stateMachines.LState
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
    playlistId: Long,
    songListManager: SongListManager,
    playlistName: String
) : StatedPlaylistViewModel(playlistName),
    Searchable<SongWrapper> {

    override val playlistFlow = songListManager.songList.map { it.asAllSongsPlaylist() }.asFlow()

    private val mModifyingPlaylist = MutableLiveData<PlaylistWrapper>()
    val modifyingPlaylist: LiveData<PlaylistWrapper>
        get() = mModifyingPlaylist

    private val songList = mutableListOf<SongWrapper>()

    /**
     * TODO Refactor this everywhere
     */
    override val stateMapper: (
        suspend (State<List<SongWrapper>>) -> State<List<SongWrapper>>
    ) = { state ->
        when (state) {
            LState.Empty -> state
            is State.Error -> state
            is State.Initializing -> state
            is State.Loading -> state
            is State.Success -> {
                State.Success(songList)
            }
            is State.Updated -> {
                State.Success(songList)
            }
        }
    }

    private val searchProcessor = SearchProcessor(QueryFilter(songQueryFilterCallback))

    init {
        model.load {
            viewModelScope.launch {
                searchProcessor.setup(
                    this@ModifyPlaylistViewModel,
                    songListManager.songList.map {
                        Data.Success(it.asAllSongsPlaylist().songList)
                    }.asFlow()
                )
                playlistListManager.getPlaylistByContainerId(playlistId).asFlow().collectLatest {
                    mModifyingPlaylist.postValue(it)
                    if (it != null) {
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

    fun confirmChanges() {
        viewModelScope.launch {
            val oldPlaylist = modifyingPlaylist.value!!
            playlistListManager.updatePlaylist(
                oldPlaylist.copy(songList = songList)
            )
        }
    }

    fun addSongToCopyOfPlaylist(song: SongWrapper) {
        songList.add(song)
    }

    fun removeSongFromCopyOfPlaylist(song: SongWrapper) {
        songList.remove(song)
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
                playlistId,
                songListManager,
                playlistName
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
