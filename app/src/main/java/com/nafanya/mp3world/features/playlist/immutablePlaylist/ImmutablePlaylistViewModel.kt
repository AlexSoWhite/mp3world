package com.nafanya.mp3world.features.playlist.immutablePlaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.listManagers.FAVOURITE_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.ListManagerProvider
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.listUtils.searching.SearchProcessor
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.core.listUtils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.listUtils.title.TitleProcessor
import com.nafanya.mp3world.core.listUtils.title.TitleViewModel
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.wrappers.LocalSong
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.favourites.FavouritesManager
import com.nafanya.mp3world.features.favourites.FavouritesManagerProxy
import com.nafanya.mp3world.features.songListViews.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.features.songListViews.SongListItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ImmutablePlaylistViewModel(
    private val favouriteListManager: FavouritesManager,
    injectedPlaylist: Flow<PlaylistWrapper>,
    baseTitle: String
) : StatedPlaylistViewModel(),
    Searchable<SongWrapper>,
    TitleViewModel<List<SongWrapper>>,
    FavouritesManagerProxy {

    override val playlistFlow = injectedPlaylist

    private val searchProcessor = SearchProcessor(QueryFilter(songQueryFilterCallback))

    private val titleProcessor = TitleProcessor<List<SongWrapper>>()
    override val title: LiveData<String>
        get() = titleProcessor.title

    init {
        model.load {
            titleProcessor.setup(model, viewModelScope)
            titleProcessor.setBaseTitle(baseTitle)
            searchProcessor.setup(
                this@ImmutablePlaylistViewModel,
                injectedPlaylist.map {
                    Data.Success(it.songList)
                }
            )
        }
    }

    override fun asListItems(list: List<SongWrapper>): List<SongListItem> {
        return list.map { SongListItem(SONG_LOCAL_IMMUTABLE, it) }
    }

    override fun search(query: String) {
        searchProcessor.search(query)
    }

    override fun isSongInFavourites(song: LocalSong) = favouriteListManager.isSongInFavourites(song)

    override fun addFavourite(song: LocalSong) {
        viewModelScope.launch {
            favouriteListManager.add(song)
        }
    }

    override fun deleteFavourite(song: LocalSong) {
        viewModelScope.launch {
            favouriteListManager.delete(song)
        }
    }

    class Factory @AssistedInject constructor(
        @Assisted("listManagerKey") private val listManagerKey: Int,
        @Assisted("containerId") private val containerId: Long,
        @Assisted("playlistName") private val playlistName: String,
        private val listManagerProvider: ListManagerProvider,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val listManager = listManagerProvider.getListManager(listManagerKey)
            val injectedPlaylist = if (listManager is FavouritesManager) {
                listManager.favorites
            } else {
                listManager.getPlaylistByContainerId(containerId).map { it }
            }
            return ImmutablePlaylistViewModel(
                listManagerProvider.getListManager(
                    FAVOURITE_LIST_MANAGER_KEY
                ) as FavouritesManager,
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
