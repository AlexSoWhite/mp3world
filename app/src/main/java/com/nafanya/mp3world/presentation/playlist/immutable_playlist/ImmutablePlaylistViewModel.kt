package com.nafanya.mp3world.presentation.playlist.immutable_playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.list_managers.FAVOURITE_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.list_managers.PlaylistProviderMapWrapper
import com.nafanya.mp3world.core.state_machines.presentation.Data
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.utils.list_utils.searching.QueryFilter
import com.nafanya.mp3world.core.utils.list_utils.searching.SearchProcessor
import com.nafanya.mp3world.core.utils.list_utils.searching.Searchable
import com.nafanya.mp3world.core.utils.list_utils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessor
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessorWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.domain.favourites.FavouritesProvider
import com.nafanya.mp3world.domain.favourites.FavouritesManager
import com.nafanya.mp3world.presentation.song_list_views.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.presentation.song_list_views.SongListItem
import com.nafanya.player.PlayerInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ImmutablePlaylistViewModel(
    playlistProviderMapWrapper: PlaylistProviderMapWrapper,
    override val playerInteractor: PlayerInteractor,
    listManagerKey: Int,
    containerId: Long,
    baseTitle: String
) : StatedPlaylistViewModel(),
    Searchable<SongWrapper>,
    TitleProcessorWrapper<List<SongWrapper>>,
    FavouritesManager {

    override val playlistFlow = playlistProviderMapWrapper
        .getPlaylistProvider(listManagerKey)
        .getPlaylistByContainerId(containerId)

    private val favouritesManager =
        (playlistProviderMapWrapper.getPlaylistProvider(FAVOURITE_LIST_MANAGER_KEY) as FavouritesProvider)

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

    override fun isSongInFavourites(song: LocalSong) = favouritesManager.observeIsSongInFavorites(song)

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
        private val playlistProviderMapWrapper: PlaylistProviderMapWrapper,
        private val playerInteractor: PlayerInteractor
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return ImmutablePlaylistViewModel(
                playlistProviderMapWrapper,
                playerInteractor,
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
