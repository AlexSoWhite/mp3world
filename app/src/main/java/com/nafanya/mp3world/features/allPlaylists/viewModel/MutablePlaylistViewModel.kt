package com.nafanya.mp3world.features.allPlaylists.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.core.listManagers.ListManagerProvider
import com.nafanya.mp3world.core.listManagers.PLAYLIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.core.listUtils.searching.SongQueryFilter
import com.nafanya.mp3world.core.viewModel.StatePlaylistViewModel
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.allPlaylists.PlaylistListManager
import com.nafanya.mp3world.features.songListViews.MODIFY_PLAYLIST_BUTTON
import com.nafanya.mp3world.features.songListViews.SONG_REARRANGEABLE
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.player.PlayerInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class MutablePlaylistViewModel(
    playerInteractor: PlayerInteractor,
    playlistListManager: PlaylistListManager,
    playlistId: Long
) : StatePlaylistViewModel(
    playerInteractor,
    playlistListManager.getPlaylistByContainerId(playlistId)
),
    Searchable<SongWrapper> {

    override val filter: QueryFilter<SongWrapper> = SongQueryFilter

    override fun List<SongWrapper>.asListItems(): List<SongListItem> {
        val list = mutableListOf<SongListItem>()
        this.forEach {
            list.add(SongListItem(SONG_REARRANGEABLE, it))
        }
        return when {
            list.isEmpty() -> listOf()
            else -> listOf(SongListItem(MODIFY_PLAYLIST_BUTTON, Unit)) + list
        }
    }

    class Factory @AssistedInject constructor(
        private val playerInteractor: PlayerInteractor,
        private val listManagerProvider: ListManagerProvider,
        @Assisted("playlistId") private val playlistId: Long
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val playlistListManager = listManagerProvider.getListManager(
                PLAYLIST_LIST_MANAGER_KEY
            ) as PlaylistListManager
            return MutablePlaylistViewModel(
                playerInteractor,
                playlistListManager,
                playlistId
            ) as T
        }

        @AssistedFactory
        interface MutablePlaylistAssistedFactory {
            fun create(@Assisted("playlistId") playlistId: Long): Factory
        }
    }
}
