package com.nafanya.mp3world.features.user_playlists.presentation.modify_playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.coroutines.collectLatestInScope
import com.nafanya.mp3world.core.list_managers.ALL_SONGS_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.list_managers.ListManagerProvider
import com.nafanya.mp3world.core.list_managers.PLAYLIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.state_machines.State
import com.nafanya.mp3world.core.state_machines.presentation.Data
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.utils.list_utils.searching.QueryFilter
import com.nafanya.mp3world.core.utils.list_utils.searching.SearchProcessor
import com.nafanya.mp3world.core.utils.list_utils.searching.Searchable
import com.nafanya.mp3world.core.utils.list_utils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessorWrapper
import com.nafanya.mp3world.core.utils.list_utils.title.convertStateToTitle
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.features.user_playlists.domain.UserPlaylistInteractor
import com.nafanya.mp3world.features.all_songs.domain.SongPlaylistProvider
import com.nafanya.mp3world.features.all_songs.domain.asAllSongsPlaylist
import com.nafanya.mp3world.features.song_list_views.SONG_ADDABLE_REMOVABLE
import com.nafanya.mp3world.features.song_list_views.SongListItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ModifyPlaylistViewModel(
    private val playlistListManager: UserPlaylistInteractor,
    private val playlistName: String,
    playlistId: Long,
    songListManager: SongPlaylistProvider
) : StatedPlaylistViewModel(),
    Searchable<SongWrapper>,
    TitleProcessorWrapper<List<SongWrapper>> {

    override val playlistFlow = songListManager.songList.map {
        it.asAllSongsPlaylist()
    }

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
                }
            )
            model.currentState.collectLatestInScope(viewModelScope, ::proceedState)
            playlistListManager
                .getPlaylistByContainerId(playlistId)
                .collectLatestInScope(viewModelScope) {
                    mModifyingPlaylist.postValue(it)
                    songList.clear()
                    songList.addAll(it.songList)
                    proceedState(model.currentState.value)
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
            ) as UserPlaylistInteractor
            val songListManager = listManagerProvider.getListManager(
                ALL_SONGS_LIST_MANAGER_KEY
            ) as SongPlaylistProvider
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
