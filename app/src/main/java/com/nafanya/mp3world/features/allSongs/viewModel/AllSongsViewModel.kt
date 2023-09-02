package com.nafanya.mp3world.features.allSongs.viewModel

import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import com.nafanya.mp3world.core.listUtils.searching.SearchProcessor
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.listUtils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.utils.timeConverters.DateConverter
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.mp3world.features.allSongs.asAllSongsPlaylist
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import com.nafanya.mp3world.features.songListViews.DATE
import com.nafanya.mp3world.features.songListViews.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.features.songListViews.SongListItem
import javax.inject.Inject

class AllSongsViewModel @Inject constructor(
    songListManager: SongListManager,
    private val mediaStoreInteractor: MediaStoreInteractor,
) : StatedPlaylistViewModel("Мои песни"),
    Searchable<SongWrapper> {

    override val playlistFlow = songListManager.songList.map { it.asAllSongsPlaylist() }.asFlow()

    private val searchProcessor = SearchProcessor(
        QueryFilter(
            songQueryFilterCallback
        )
    )

    init {
        model.load {
            searchProcessor.setup(
                this@AllSongsViewModel,
                songListManager.songList.map {
                    Data.Success(it.asAllSongsPlaylist().songList)
                }.asFlow()
            )
        }
    }

    fun refresh() {
        model.refresh {
            mediaStoreInteractor.reset()
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun asListItems(list: List<SongWrapper>): List<SongListItem> {
        val listItemMap: MutableMap<String, MutableList<LocalSong>> = mutableMapOf()
        val listItems = mutableListOf<SongListItem>()
        (list as List<LocalSong>)
            .groupByTo(listItemMap) { song ->
                DateConverter().dateToString(song.date)
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
