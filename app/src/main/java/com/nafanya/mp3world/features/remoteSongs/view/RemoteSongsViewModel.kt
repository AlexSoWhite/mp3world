package com.nafanya.mp3world.features.remoteSongs.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.nafanya.mp3world.core.viewModel.StatePlaylistViewModel
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.remoteSongs.QueryExecutor
import com.nafanya.mp3world.features.songListViews.SONG_REMOTE
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.player.PlayerInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class RemoteSongsViewModel(
    private val query: String,
    queryExecutor: QueryExecutor,
    playerInteractor: PlayerInteractor
) : StatePlaylistViewModel(
    playerInteractor,
    queryExecutor.songList.map {
        it?.let { list ->
            PlaylistWrapper(list, -1, query)
        } ?: PlaylistWrapper(emptyList(), -1, query)
    }
) {

    init {
        queryExecutor.executeQuery(query)
    }

    override fun List<SongWrapper>.asListItems(): List<SongListItem> {
        return this.map {
            SongListItem(SONG_REMOTE, it)
        }
    }

    class Factory @AssistedInject constructor(
        @Assisted("query") private val query: String,
        private val playerInteractor: PlayerInteractor,
        private val queryExecutor: QueryExecutor
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RemoteSongsViewModel(query, queryExecutor, playerInteractor) as T
        }

        @AssistedFactory
        interface RemoteAssistedFactory {
            fun create(@Assisted("query") query: String): Factory
        }
    }
}
