package com.nafanya.mp3world.features.playlist.immutablePlaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.listManagers.FAVOURITE_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.ListManagerProvider
import com.nafanya.mp3world.core.stateMachines.commonUi.Data
import com.nafanya.mp3world.core.stateMachines.commonUi.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.utils.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.utils.listUtils.searching.SearchProcessor
import com.nafanya.mp3world.core.utils.listUtils.searching.Searchable
import com.nafanya.mp3world.core.utils.listUtils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.utils.listUtils.title.TitleProcessor
import com.nafanya.mp3world.core.utils.listUtils.title.TitleProcessorWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.features.favourites.FavouritesManager
import com.nafanya.mp3world.features.favourites.FavouritesManagerProxy
import com.nafanya.mp3world.features.songListViews.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.features.songListViews.SongListItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ImmutablePlaylistViewModel(
    listManagerProvider: ListManagerProvider,
    listManagerKey: Int,
    containerId: Long,
    baseTitle: String
) : StatedPlaylistViewModel(),
    Searchable<SongWrapper>,
    TitleProcessorWrapper<List<SongWrapper>>,
    FavouritesManagerProxy {

    override val playlistFlow = listManagerProvider
        .getListManager(listManagerKey)
        .getPlaylistByContainerId(containerId)

    private val favouritesManager =
        (listManagerProvider.getListManager(FAVOURITE_LIST_MANAGER_KEY) as FavouritesManager)

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
                playlistFlow.map {
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

    override fun isSongInFavourites(song: LocalSong) = favouritesManager.isSongInFavourites(song)

    override fun addFavourite(song: LocalSong) {
        viewModelScope.launch {
            favouritesManager.add(song)
        }
    }

    override fun deleteFavourite(song: LocalSong) {
        viewModelScope.launch {
            favouritesManager.delete(song)
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

            return ImmutablePlaylistViewModel(
                listManagerProvider,
                listManagerKey,
                containerId,
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
