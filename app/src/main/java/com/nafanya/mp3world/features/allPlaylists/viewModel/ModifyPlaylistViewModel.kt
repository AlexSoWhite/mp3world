package com.nafanya.mp3world.features.allPlaylists.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.listManagers.ALL_SONGS_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.ListManagerProvider
import com.nafanya.mp3world.core.listManagers.PLAYLIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.core.listUtils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.viewModel.StatePlaylistViewModel
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.allPlaylists.PlaylistListManager
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.mp3world.features.allSongs.asAllSongsPlaylist
import com.nafanya.mp3world.features.songListViews.SONG_ADDABLE_REMOVABLE
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.player.PlayerInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class ModifyPlaylistViewModel(
    playlistId: Long,
    private val playlistListManager: PlaylistListManager,
    songListManager: SongListManager,
    playerInteractor: PlayerInteractor
) : StatePlaylistViewModel(
    playerInteractor,
    songListManager.songList.map { it.asAllSongsPlaylist() }
),
    Searchable<SongWrapper> {

    override val filter: QueryFilter<SongWrapper> = QueryFilter(songQueryFilterCallback)

    private val mModifyingPlaylist = MutableLiveData<PlaylistWrapper>()
    val modifyingPlaylist: LiveData<PlaylistWrapper>
        get() = mModifyingPlaylist

    private var songList: MutableLiveData<List<SongWrapper>> = MutableLiveData()

    private var modifyingPlaylistTitle = ""
    private var songListSize = 0

    init {
        applyFilter(this)
        with(compositeObservable) {
            addObserver(playlistListManager.getPlaylistByContainerId(playlistId)) {
                it?.let { mModifyingPlaylist.value = it }
            }
            addObserver(modifyingPlaylist) {
                modifyingPlaylistTitle = it.name
                songList.value = it.songList
            }
            addObserver(songList) {
                songListSize = it.size
            }
        }
        resetContainerSizeSource(songList.map { it.size })
        addStateUpdateTrigger(songList)
    }

    override fun List<SongWrapper>.asListItems(): List<SongListItem> {
        return this.map {
            SongListItem(SONG_ADDABLE_REMOVABLE, it)
        }
    }

    override fun renderListEmptyTitle() = title()
    override fun renderListLoadedTitle() = title()
    override fun renderListLoadingTitle() = title()
    override fun renderListUpdatedTitle() = title()

    private fun title() = "$modifyingPlaylistTitle ($songListSize)"

    fun confirmChanges() {
        viewModelScope.launch {
            playlistListManager.updatePlaylist(
                modifyingPlaylist.value!!.copy(songList = songList.value!!)
            )
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

    class Factory @AssistedInject constructor(
        @Assisted("playlistId") private val playlistId: Long,
        private val playerInteractor: PlayerInteractor,
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
                playerInteractor
            ) as T
        }

        @AssistedFactory
        interface ModifyPlaylistFactory {

            fun create(@Assisted("playlistId") playlistId: Long): Factory
        }
    }
}
