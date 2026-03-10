package com.nafanya.mp3world.features.all_songs.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.state_machines.presentation.Data
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.utils.list_utils.searching.QueryFilter
import com.nafanya.mp3world.core.utils.list_utils.searching.SearchProcessor
import com.nafanya.mp3world.core.utils.list_utils.searching.Searchable
import com.nafanya.mp3world.core.utils.list_utils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessor
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessorWrapper
import com.nafanya.mp3world.core.utils.time_converters.DateConverter
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.features.all_songs.domain.SongPlaylistProvider
import com.nafanya.mp3world.features.all_songs.domain.asAllSongsPlaylist
import com.nafanya.mp3world.features.favourites.domain.FavouritesProvider
import com.nafanya.mp3world.features.favourites.domain.FavouritesManagerProxy
import com.nafanya.mp3world.data.media_store.MediaStoreInteractor
import com.nafanya.mp3world.features.song_list_views.DATE
import com.nafanya.mp3world.features.song_list_views.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.features.song_list_views.SongListItem
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// todo: maybe I should not use so many abstractions
// но это не точно
class AllSongsViewModel @Inject constructor(
    private val mediaStoreInteractor: MediaStoreInteractor,
    private val favouritesManager: FavouritesProvider,
    songListManager: SongPlaylistProvider
) : StatedPlaylistViewModel(),
    Searchable<SongWrapper>,
    TitleProcessorWrapper<List<SongWrapper>>,
    FavouritesManagerProxy {

    override val playlistFlow = songListManager.songList.map {
        it.asAllSongsPlaylist()
    }

    private val searchProcessor = SearchProcessor(
        QueryFilter(
            songQueryFilterCallback
        )
    )

    private val titleProcessor = TitleProcessor<List<SongWrapper>>()
    override val title: LiveData<String>
        get() = titleProcessor.title

    init {
        model.load {
            titleProcessor.setup(model, viewModelScope)
            // TODO string resource
            titleProcessor.setBaseTitle("Мои песни")
            searchProcessor.setup(
                this@AllSongsViewModel,
                songListManager.songList.map {
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
                DateConverter.dateToString(song.date)
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
