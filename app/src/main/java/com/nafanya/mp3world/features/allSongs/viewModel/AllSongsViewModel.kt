package com.nafanya.mp3world.features.allSongs.viewModel

import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.listUtils.searching.SearchableStated
import com.nafanya.mp3world.core.listUtils.searching.StatedQueryFilter
import com.nafanya.mp3world.core.listUtils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.utils.timeConverters.DateConverter
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.mp3world.features.allSongs.asAllSongsPlaylist
import com.nafanya.mp3world.features.songListViews.DATE
import com.nafanya.mp3world.features.songListViews.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AllSongsViewModel @Inject constructor(
    playerInteractor: PlayerInteractor,
    songListManager: SongListManager
) : StatedPlaylistViewModel(
    playerInteractor,
    songListManager.songList.map { it.asAllSongsPlaylist() }.asFlow(),
    "Мои песни"
),
    SearchableStated<SongWrapper> {

    override val queryFilter: StatedQueryFilter<SongWrapper> = StatedQueryFilter(
        songQueryFilterCallback
    )

    init {
        model.load {
            viewModelScope.launch {
                setDataSourceFiltered(
                    songListManager.songList.asFlow().map {
                        Data.Success(it.asAllSongsPlaylist().songList)
                    }
                )
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun asListItems(list: List<SongWrapper>): List<SongListItem> {
        val listItemMap: MutableMap<String, MutableList<LocalSong>> = mutableMapOf()
        val listItems = mutableListOf<SongListItem>()
        (list as List<LocalSong>).groupByTo(listItemMap) { song ->
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
}
