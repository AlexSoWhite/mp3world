package com.nafanya.mp3world.presentation.all_songs

import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.state_machines.presentation.Data
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.utils.list_utils.searching.QueryFilter
import com.nafanya.mp3world.core.utils.list_utils.searching.SearchProcessor
import com.nafanya.mp3world.core.utils.list_utils.searching.Searchable
import com.nafanya.mp3world.core.utils.list_utils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.utils.list_utils.title.TitleModel
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessor
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessorWrapper
import com.nafanya.mp3world.core.utils.time_converters.DateConverter
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.domain.all_songs.SongPlaylistProvider
import com.nafanya.mp3world.domain.all_songs.asAllSongsPlaylist
import com.nafanya.mp3world.domain.favourites.FavouritesProvider
import com.nafanya.mp3world.domain.favourites.FavouritesManager
import com.nafanya.mp3world.data.media_store.MediaStoreInteractor
import com.nafanya.mp3world.presentation.song_list_views.DATE
import com.nafanya.mp3world.presentation.song_list_views.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.presentation.song_list_views.SongListItem
import com.nafanya.player.interactor.PlayerInteractor
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// todo: maybe I should not use so many abstractions
// но это не точно
class AllSongsViewModel @Inject constructor(
    private val mediaStoreInteractor: MediaStoreInteractor,
    private val favouritesManager: FavouritesProvider,
    override val playerInteractor: PlayerInteractor,
    songListProvider: SongPlaylistProvider
) : StatedPlaylistViewModel(),
    Searchable<SongWrapper>,
    TitleProcessorWrapper<List<SongWrapper>>,
    FavouritesManager {

    override val playlistFlow = songListProvider.songList.map {
        it.asAllSongsPlaylist()
    }

    private val searchProcessor = SearchProcessor(
        QueryFilter(
            songQueryFilterCallback
        )
    )

    private val titleProcessor = TitleProcessor<List<SongWrapper>>(
        baseTitleRes = R.string.my_songs
    )
    override val title: StateFlow<TitleModel>
        get() = titleProcessor.title

    init {
        model.load {
            titleProcessor.setup(model, viewModelScope)
            searchProcessor.setup(
                this@AllSongsViewModel,
                songListProvider.songList.map {
                    Data.Success(it.asAllSongsPlaylist().songList)
                }
            )
        }
    }

    fun refresh() {
        model.refresh {
            mediaStoreInteractor.reset()
        }
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

    @Suppress("UNCHECKED_CAST")
    override fun asListItems(list: List<SongWrapper>): List<SongListItem> {
        val listItemMap: MutableMap<String, MutableList<LocalSong>> = mutableMapOf()
        val listItems = mutableListOf<SongListItem>()
        (list as List<LocalSong>)
            .groupByTo(listItemMap) { song ->
                DateConverter.millisecondsToDateString(song.date)
            }
        listItemMap.entries.forEach { entry ->
            val date = entry.key
            val songs = entry.value
            listItems.add(SongListItem(DATE, date))
            songs.forEach { groupedSong ->
                listItems.add(SongListItem(SONG_LOCAL_IMMUTABLE, groupedSong))
            }
        }
        return listItems
    }

    override fun search(query: String) {
        searchProcessor.search(query)
    }
}
