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
import com.nafanya.mp3world.core.listUtils.searching.SearchableStated
import com.nafanya.mp3world.core.listUtils.searching.StatedQueryFilter
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class ModifyPlaylistViewModel(
    private val playlistId: Long,
    private val playlistListManager: PlaylistListManager,
    songListManager: SongListManager,
    private val playlistName: String
) : StatedPlaylistViewModel(playlistName),
    SearchableStated<SongWrapper> {

    override val playlistFlow = songListManager.songList.map { it.asAllSongsPlaylist() }.asFlow()

    override val queryFilter: StatedQueryFilter<SongWrapper> =
        StatedQueryFilter(songQueryFilterCallback)

    private val mModifyingPlaylist = MutableLiveData(getDefaultPlaylistWrapper())
    val modifyingPlaylist: LiveData<PlaylistWrapper>
        get() = mModifyingPlaylist

    private var songList: MutableLiveData<List<SongWrapper>> = MutableLiveData()

    override val stateMapper: (
        suspend (State<List<SongWrapper>>) -> State<List<SongWrapper>>
    ) = { state ->
        when (state) {
            LState.Empty -> state
            is State.Error -> state
            is State.Initializing -> state
            is State.Loading -> state
            is State.Success -> {
                State.Success(songList.asFlow().first())
            }
            is State.Updated -> {
                State.Success(songList.asFlow().first())
            }
        }
    }

    init {
        model.load {
            viewModelScope.launch {
                setDataSourceFiltered(
                    songListManager.songList.asFlow().map {
                        Data.Success(it.asAllSongsPlaylist().songList)
                    }
                )
                playlistListManager.getPlaylistByContainerId(playlistId).asFlow().collect {
                    mModifyingPlaylist.postValue(it)
                    if (it != null) {
                        songList.postValue(it.songList)
                        Data.Success(it.songList)
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

    fun confirmChanges() {
        viewModelScope.launch {
            playlistListManager.updatePlaylist(
                modifyingPlaylist.value!!.copy(songList = songList.value!!)
            )
            // to prevent crash if media item doesn't exist
            playerInteractor.currentPlaylist.asFlow().take(1).collectLatest {
                if (it == modifyingPlaylist.value!!) {
                    playerInteractor.currentSong.collectLatest { newSong ->
                        if (newSong != null && songList.value!!.contains(newSong)) {
                            playerInteractor.setPlaylist(it)
                            playerInteractor.setSong(newSong)
                        } else if (newSong != null && songList.value!!.isNotEmpty()) {
                            playerInteractor.setPlaylist(it)
                            playerInteractor.setSong(songList.value!![0])
                        } else if (newSong != null) {
                            playerInteractor.setPlaylist(
                                PlaylistWrapper(
                                    songList = listOf(newSong as SongWrapper),
                                    name = "Temporary"
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun addSongToPlaylist(song: SongWrapper) {
        val list = mutableListOf(song)
        val existedList = songList.value ?: listOf()
        songList.value = list + existedList
    }

    fun removeSongFromPlaylist(song: SongWrapper) {
        val list = songList.value as MutableList<SongWrapper>
        list.remove(song)
        songList.value = list
    }

    private fun getDefaultPlaylistWrapper(): PlaylistWrapper {
        return PlaylistWrapper(
            listOf(),
            playlistId,
            playlistName,
            position = -1,
            imageSource = null
        )
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
                playlistId,
                playlistListManager,
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
