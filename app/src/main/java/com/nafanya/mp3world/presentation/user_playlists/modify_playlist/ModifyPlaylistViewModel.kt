package com.nafanya.mp3world.presentation.user_playlists.modify_playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.coroutines.collectLatestInScope
import com.nafanya.mp3world.core.list_managers.ALL_SONGS_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.list_managers.PlaylistProviderMapWrapper
import com.nafanya.mp3world.core.list_managers.PLAYLIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.state_machines.State
import com.nafanya.mp3world.core.state_machines.presentation.Data
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.utils.list_utils.searching.QueryFilter
import com.nafanya.mp3world.core.utils.list_utils.searching.SearchProcessor
import com.nafanya.mp3world.core.utils.list_utils.searching.Searchable
import com.nafanya.mp3world.core.utils.list_utils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.utils.list_utils.title.TitleModel
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessorWrapper
import com.nafanya.mp3world.core.utils.list_utils.title.convertStateToCount
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.domain.user_playlists.UserPlaylistsInteractor
import com.nafanya.mp3world.domain.all_songs.SongPlaylistProvider
import com.nafanya.mp3world.domain.all_songs.asAllSongsPlaylist
import com.nafanya.mp3world.presentation.song_list_views.SONG_ADDABLE_REMOVABLE
import com.nafanya.mp3world.presentation.song_list_views.SongListItem
import com.nafanya.player.interactor.PlayerInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ModifyPlaylistViewModel(
    private val userPlaylistsInteractor: UserPlaylistsInteractor,
    override val playerInteractor: PlayerInteractor,
    private val playlistName: String,
    playlistId: Long,
    songListProvider: SongPlaylistProvider
) : StatedPlaylistViewModel(),
    Searchable<SongWrapper>,
    TitleProcessorWrapper<List<SongWrapper>> {

    override val playlistFlow = songListProvider.songList.map {
        it.asAllSongsPlaylist()
    }

    private val _playlistUnderModification = MutableSharedFlow<PlaylistWrapper>(replay = 1)
    val playlistUnderModification: SharedFlow<PlaylistWrapper>
        get() = _playlistUnderModification

    private val songList = mutableListOf<SongWrapper>()

    private val searchProcessor = SearchProcessor(QueryFilter(songQueryFilterCallback))

    private val _title = MutableStateFlow(TitleModel.Builder().withBaseString(playlistName).build())
    override val title: StateFlow<TitleModel>
        get() = _title

    init {
        model.load {
            searchProcessor.setup(
                this@ModifyPlaylistViewModel,
                songListProvider.songList.map {
                    Data.Success(it.asAllSongsPlaylist().songList)
                }
            )
            model.currentState.collectLatestInScope(viewModelScope, ::processState)
            userPlaylistsInteractor
                .getPlaylistByContainerId(playlistId)
                .collectLatestInScope(viewModelScope) {
                    viewModelScope.launch {
                        _playlistUnderModification.emit(it)
                        songList.clear()
                        songList.addAll(it.songList)
                        processState(model.currentState.value)
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

    private fun processState(state: State<List<SongWrapper>>) {
        val newState = when (state) {
            is State.Success, is State.Updated -> State.Success(songList)
            else -> state
        }
        val builder = TitleModel.Builder().withBaseString(playlistName)
        val count = convertStateToCount(newState)
        if (count != null) {
            builder.withCount(count)
        }
        _title.value = builder.build()
    }

    fun confirmChanges() {
        val oldPlaylist = playlistUnderModification.replayCache[0]
        viewModelScope.launch {
            userPlaylistsInteractor.updatePlaylist(
                oldPlaylist.copy(songList = songList)
            )
        }
    }

    fun addSongToCopyOfPlaylist(song: SongWrapper) {
        songList.add(0, song)
        processState(model.currentState.value)
    }

    fun removeSongFromCopyOfPlaylist(song: SongWrapper) {
        songList.remove(song)
        processState(model.currentState.value)
    }

    class Factory @AssistedInject constructor(
        @Assisted("playlistId") private val playlistId: Long,
        @Assisted("playlistName") private val playlistName: String,
        private val playlistProviderMapWrapper: PlaylistProviderMapWrapper,
        private val playerInteractor: PlayerInteractor
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(playlistId > -1)
            val playlistListManager = playlistProviderMapWrapper.getPlaylistProvider(
                PLAYLIST_LIST_MANAGER_KEY
            ) as UserPlaylistsInteractor
            val songListManager = playlistProviderMapWrapper.getPlaylistProvider(
                ALL_SONGS_LIST_MANAGER_KEY
            ) as SongPlaylistProvider
            return ModifyPlaylistViewModel(
                playlistListManager,
                playerInteractor,
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
