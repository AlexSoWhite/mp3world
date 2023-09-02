package com.nafanya.mp3world.features.playlist.immutablePlaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import com.nafanya.mp3world.core.listManagers.ListManagerProvider
import com.nafanya.mp3world.core.listUtils.searching.SearchProcessor
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.listUtils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.favorites.FavouriteListManager
import com.nafanya.mp3world.features.songListViews.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.features.songListViews.SongListItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ImmutablePlaylistViewModel(
    injectedPlaylist: Flow<PlaylistWrapper>,
    baseTitle: String
) : StatedPlaylistViewModel(baseTitle),
    Searchable<SongWrapper> {

    override val playlistFlow = injectedPlaylist

    override fun asListItems(list: List<SongWrapper>): List<SongListItem> {
        return list.map { SongListItem(SONG_LOCAL_IMMUTABLE, it) }
    }

    private val searchProcessor = SearchProcessor(QueryFilter(songQueryFilterCallback))

    init {
        model.load {
            searchProcessor.setup(
                this@ImmutablePlaylistViewModel,
                injectedPlaylist.map {
                    Data.Success(it.songList)
                }
            )
        }
    }

    override fun search(query: String) {
        searchProcessor.search(query)
    }

    class Factory @AssistedInject constructor(
        @Assisted("listManagerKey") private val listManagerKey: Int,
        @Assisted("containerId") private val containerId: Long,
        @Assisted("playlistName") private val playlistName: String,
        private val listManagerProvider: ListManagerProvider
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val listManager = listManagerProvider.getListManager(listManagerKey)
            val injectedPlaylist = if (listManager is FavouriteListManager) {
                listManager.favorites
            } else {
                listManager.getPlaylistByContainerId(containerId).map { it }
            }
            return ImmutablePlaylistViewModel(
                injectedPlaylist.map { it }.asFlow() as Flow<PlaylistWrapper>,
                playlistName
            ) as T
        }

        @AssistedFactory
        interface AssistedPlaylistFactory {

            fun create(
                @Assisted("listManagerKey") listManagerKey: Int,
                @Assisted("containerId") containerId: Long,
                @Assisted("playlistName") playlistName: String
            ): Factory
        }
    }
}
