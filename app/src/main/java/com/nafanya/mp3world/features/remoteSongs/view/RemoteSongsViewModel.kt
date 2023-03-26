package com.nafanya.mp3world.features.remoteSongs.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.remoteSongs.QueryExecutor
import com.nafanya.mp3world.features.remoteSongs.asPlaylist
import com.nafanya.mp3world.features.songListViews.SONG_REMOTE
import com.nafanya.mp3world.features.songListViews.SongListItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class RemoteSongsViewModel(
    private val query: String,
    private val queryExecutor: QueryExecutor
) : StatedPlaylistViewModel(
    queryExecutor.songList.map { it.asPlaylist(query) }.asFlow(),
    query
) {

    init {
        model.load {
            queryExecutor.executeQuery(query)
            setDataSource(
                queryExecutor.songList.map {
                    if (it == null) {
                        Data.Error(Error("no internet"))
                    } else {
                        Data.Success(it.asPlaylist(query).songList)
                    }
                }.asFlow()
            )
            viewModelScope.launch {
                mIsInteractorBound.collect {
                    if (it) {
                        playerInteractor.isPlayerInitialised.collect { isInit ->
                            if (!isInit) {
                                queryExecutor.songList.asFlow().collect { songList ->
                                    if (songList?.isNotEmpty() == true) {
                                        playerInteractor.setPlaylist(songList.asPlaylist(query))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun asListItems(list: List<SongWrapper>): List<SongListItem> {
        return list.map { SongListItem(SONG_REMOTE, it) }
    }

    fun refresh() {
        model.refresh {
            queryExecutor.executeQuery(query)
        }
    }

    class Factory @AssistedInject constructor(
        @Assisted("query") private val query: String,
        private val queryExecutor: QueryExecutor
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RemoteSongsViewModel(query, queryExecutor) as T
        }

        @AssistedFactory
        interface RemoteAssistedFactory {
            fun create(@Assisted("query") query: String): Factory
        }
    }
}
