package com.nafanya.mp3world.features.allSongs.viewModel

import androidx.lifecycle.map
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.core.listUtils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.utils.timeConverters.DateConverter
import com.nafanya.mp3world.core.viewModel.StatePlaylistViewModel
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.mp3world.features.allSongs.asAllSongsPlaylist
import com.nafanya.mp3world.features.songListViews.DATE
import com.nafanya.mp3world.features.songListViews.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject

class AllSongsViewModel @Inject constructor(
    playerInteractor: PlayerInteractor,
    songListManager: SongListManager
) : StatePlaylistViewModel(
    playerInteractor,
    songListManager.songList.map { it.asAllSongsPlaylist() }
),
    Searchable<SongWrapper> {

    override val filter: QueryFilter<SongWrapper> = QueryFilter(songQueryFilterCallback)

    init {
        applyFilter(this)
    }

    @Suppress("UNCHECKED_CAST")
    override fun List<SongWrapper>.asListItems(): List<SongListItem> {
        val listItemMap: MutableMap<String, MutableList<LocalSong>> = mutableMapOf()
        val listItems = mutableListOf<SongListItem>()
        (this as List<LocalSong>).groupByTo(listItemMap) { song ->
            DateConverter().dateToString(song.date)
        }
        listItemMap.entries.forEach { entry ->
            val date = entry.key
            val list = entry.value
            listItems.add(SongListItem(DATE, date))
            list.forEach { groupedSong ->
                listItems.add(SongListItem(SONG_LOCAL_IMMUTABLE, groupedSong))
            }
        }
        return listItems
    }
}
