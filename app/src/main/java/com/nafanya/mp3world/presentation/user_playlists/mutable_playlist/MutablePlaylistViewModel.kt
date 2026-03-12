package com.nafanya.mp3world.presentation.user_playlists.mutable_playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.list_managers.FAVOURITE_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.list_managers.PlaylistProviderMapWrapper
import com.nafanya.mp3world.core.list_managers.PLAYLIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.state_machines.presentation.Data
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.utils.list_utils.searching.QueryFilter
import com.nafanya.mp3world.core.utils.list_utils.searching.SearchProcessor
import com.nafanya.mp3world.core.utils.list_utils.searching.Searchable
import com.nafanya.mp3world.core.utils.list_utils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.utils.list_utils.title.TitleModel
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessor
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessorWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.domain.user_playlists.UserPlaylistsInteractor
import com.nafanya.mp3world.domain.favourites.FavouritesProvider
import com.nafanya.mp3world.domain.favourites.FavouritesManager
import com.nafanya.mp3world.presentation.song_list_views.MODIFY_PLAYLIST_BUTTON
import com.nafanya.mp3world.presentation.song_list_views.SONG_REARRANGEABLE
import com.nafanya.mp3world.presentation.song_list_views.SongListItem
import com.nafanya.player.interactor.PlayerInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MutablePlaylistViewModel(
    playlistProviderMapWrapper: PlaylistProviderMapWrapper,
    override val playerInteractor: PlayerInteractor,
    playlistId: Long,
    playlistName: String
) : StatedPlaylistViewModel(),
    Searchable<SongWrapper>,
    TitleProcessorWrapper<List<SongWrapper>>,
    FavouritesManager {

    private val playlistListManager = playlistProviderMapWrapper.getPlaylistProvider(
        PLAYLIST_LIST_MANAGER_KEY
    ) as UserPlaylistsInteractor

    private val favouritesManager = playlistProviderMapWrapper.getPlaylistProvider(
        FAVOURITE_LIST_MANAGER_KEY
    ) as FavouritesProvider

    override val playlistFlow = playlistListManager.getPlaylistByContainerId(playlistId)

    private val searchProcessor = SearchProcessor(QueryFilter(songQueryFilterCallback))

    private val titleProcessor = TitleProcessor<List<SongWrapper>>(
        baseTitleString = playlistName
    )
    override val title: StateFlow<TitleModel>
        get() = titleProcessor.title

    init {
        model.load {
            titleProcessor.setup(model, viewModelScope)
            searchProcessor.setup(
                this,
                playlistListManager.getPlaylistByContainerId(playlistId).map {
                    Data.Success(it.songList)
                }
            )
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

    override fun search(query: String) {
        searchProcessor.search(query)
    }

    override fun isSongInFavourites(song: LocalSong) = favouritesManager.observeIsSongInFavorites(song)

    override fun addFavourite(song: LocalSong) {
        viewModelScope.launch { favouritesManager.add(song) }
    }

    override fun deleteFavourite(song: LocalSong) {
        viewModelScope.launch { favouritesManager.delete(song) }
    }

    class Factory @AssistedInject constructor(
        private val playlistProviderMapWrapper: PlaylistProviderMapWrapper,
        private val playerInteractor: PlayerInteractor,
        @Assisted("playlistId") private val playlistId: Long,
        @Assisted("playlistName") private var playlistName: String
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MutablePlaylistViewModel(
                playlistProviderMapWrapper,
                playerInteractor,
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
