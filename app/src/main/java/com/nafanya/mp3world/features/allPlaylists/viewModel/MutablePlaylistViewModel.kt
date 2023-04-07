package com.nafanya.mp3world.features.allPlaylists.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.listManagers.ListManagerProvider
import com.nafanya.mp3world.core.listManagers.PLAYLIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listUtils.searching.SearchableStated
import com.nafanya.mp3world.core.listUtils.searching.StatedQueryFilter
import com.nafanya.mp3world.core.listUtils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.allPlaylists.PlaylistListManager
import com.nafanya.mp3world.features.songListViews.MODIFY_PLAYLIST_BUTTON
import com.nafanya.mp3world.features.songListViews.SONG_REARRANGEABLE
import com.nafanya.mp3world.features.songListViews.SongListItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MutablePlaylistViewModel(
    playlistListManager: PlaylistListManager,
    playlistId: Long,
    playlistName: String
) : StatedPlaylistViewModel(baseTitle = playlistName),
    SearchableStated<SongWrapper> {

    override val playlistFlow = playlistListManager
        .getPlaylistByContainerId(playlistId)
        .asFlow()
        .map {
            it ?: throw IllegalArgumentException("playlist doesn't exist")
        }

    override val queryFilter: StatedQueryFilter<SongWrapper> = StatedQueryFilter(
        songQueryFilterCallback
    )

    init {
        model.load {
            viewModelScope.launch {
                setDataSourceFiltered(
                    playlistListManager.getPlaylistByContainerId(playlistId).map {
                        if (it != null) {
                            Data.Success(it.songList)
                        } else {
                            Data.Error(java.lang.Error("unknown playlist"))
                        }
                    }.asFlow()
                )
            }
        }
    }

    override fun asListItems(list: List<SongWrapper>): List<SongListItem> {
        val itemList = mutableListOf<SongListItem>()
        list.forEach {
            itemList.add(SongListItem(SONG_REARRANGEABLE, it))
        }
        return when {
            itemList.isEmpty() -> listOf()
            else -> listOf(SongListItem(MODIFY_PLAYLIST_BUTTON, Unit)) + itemList
        }
    }

    class Factory @AssistedInject constructor(
        private val listManagerProvider: ListManagerProvider,
        @Assisted("playlistId") private val playlistId: Long,
        @Assisted("playlistName") private var playlistName: String
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val playlistListManager = listManagerProvider.getListManager(
                PLAYLIST_LIST_MANAGER_KEY
            ) as PlaylistListManager
            return MutablePlaylistViewModel(
                playlistListManager,
                playlistId,
                playlistName
            ) as T
        }

        @AssistedFactory
        interface MutablePlaylistAssistedFactory {
            fun create(
                @Assisted("playlistId") playlistId: Long,
                @Assisted("playlistName") playlistName: String
            ): Factory
        }
    }
}
