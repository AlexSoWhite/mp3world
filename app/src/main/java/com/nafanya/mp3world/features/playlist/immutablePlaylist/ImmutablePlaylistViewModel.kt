package com.nafanya.mp3world.features.playlist.immutablePlaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.nafanya.mp3world.core.listManagers.ListManagerProvider
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.core.viewModel.StatePlaylistViewModel
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.favorites.FavouriteListManager
import com.nafanya.mp3world.features.songListViews.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.player.PlayerInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ImmutablePlaylistViewModel(
    injectedPlaylist: LiveData<PlaylistWrapper?>,
    playerInteractor: PlayerInteractor
) : StatePlaylistViewModel(
    playerInteractor,
    injectedPlaylist
),
    Searchable<SongWrapper> {

    override fun List<SongWrapper>.asListItems(): List<SongListItem> {
        return this.map { SongListItem(SONG_LOCAL_IMMUTABLE, it) }
    }

    override val filter: QueryFilter<SongWrapper> = QueryFilter { song, query ->
        song.title.contains(query, true) ||
            song.artist.contains(query, true)
    }

    init {
        applyFilter(this)
    }

    class Factory @AssistedInject constructor(
        @Assisted("listManagerKey") private val listManagerKey: Int,
        @Assisted("containerId") private val containerId: Long,
        private val playerInteractor: PlayerInteractor,
        private val listManagerProvider: ListManagerProvider
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val listManager = listManagerProvider.getListManager(listManagerKey)
            val injectedPlaylist = if (listManager is FavouriteListManager) {
                listManager.favorites
            } else {
                listManager.getPlaylistByContainerId(containerId).map { it }
            }
            return ImmutablePlaylistViewModel(
                injectedPlaylist.map { it },
                playerInteractor
            ) as T
        }

        @AssistedFactory
        interface AssistedPlaylistFactory {

            fun create(
                @Assisted("listManagerKey") listManagerKey: Int,
                @Assisted("containerId") containerId: Long
            ): Factory
        }
    }
}
