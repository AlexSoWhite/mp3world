package com.nafanya.mp3world.features.allPlaylists.mutablePlaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.listManagers.ListManagerProvider
import com.nafanya.mp3world.core.listManagers.PLAYLIST_LIST_MANAGER_KEY
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
import com.nafanya.mp3world.features.allPlaylists.PlaylistListManager
import com.nafanya.mp3world.features.favourites.FavouritesManager
import com.nafanya.mp3world.features.favourites.FavouritesManagerProxy
import com.nafanya.mp3world.features.songListViews.MODIFY_PLAYLIST_BUTTON
import com.nafanya.mp3world.features.songListViews.SONG_REARRANGEABLE
import com.nafanya.mp3world.features.songListViews.SongListItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MutablePlaylistViewModel(
    private val favouriteListManager: FavouritesManager,
    playlistListManager: PlaylistListManager,
    playlistId: Long,
    playlistName: String
) : StatedPlaylistViewModel(),
    Searchable<SongWrapper>,
    TitleProcessorWrapper<List<SongWrapper>>,
    FavouritesManagerProxy {

    override val playlistFlow = playlistListManager.getPlaylistByContainerId(playlistId)

    private val searchProcessor = SearchProcessor(QueryFilter(songQueryFilterCallback))

    private val titleProcessor = TitleProcessor<List<SongWrapper>>()
    override val title: LiveData<String>
        get() = titleProcessor.title

    init {
        model.load {
            titleProcessor.setup(model, viewModelScope)
            titleProcessor.setBaseTitle(playlistName)
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

    override fun isSongInFavourites(song: LocalSong) = favouriteListManager.isSongInFavourites(song)

    override fun addFavourite(song: LocalSong) {
        viewModelScope.launch { favouriteListManager.add(song) }
    }

    override fun deleteFavourite(song: LocalSong) {
        viewModelScope.launch { favouriteListManager.delete(song) }
    }

    class Factory @AssistedInject constructor(
        private val favouriteListManager: FavouritesManager,
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
                favouriteListManager,
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
