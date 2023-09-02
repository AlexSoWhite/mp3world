package com.nafanya.mp3world.features.remoteSongs.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.mediaStore.MediaStoreInteractor
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong
import com.nafanya.mp3world.features.downloading.DownloadInteractor
import com.nafanya.mp3world.features.downloading.DownloadingViewModel
import com.nafanya.mp3world.features.remoteSongs.songSearchers.HITMO_TOP
import com.nafanya.mp3world.features.remoteSongs.songSearchers.SongSearcher
import com.nafanya.mp3world.features.remoteSongs.songSearchers.SongSearchersProvider
import com.nafanya.mp3world.features.songListViews.SONG_REMOTE
import com.nafanya.mp3world.features.songListViews.SongListItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RemoteSongsViewModel(
    private val query: String,
    private val songSearchersProvider: SongSearchersProvider,
    override val downloadInteractor: DownloadInteractor,
    override val mediaStoreInteractor: MediaStoreInteractor
) : StatedPlaylistViewModel(baseTitle = query),
    DownloadingViewModel {

    private val songSearcher: SongSearcher
        get() = songSearchersProvider.getSongSearcher(HITMO_TOP)

    override val playlistFlow: Flow<PlaylistWrapper>
        get() = songSearcher.songList.map { it.asPlaylist(query) }

    init {
        model.load {
            songSearcher.searchSongs(query)
            setDataSource(
                songSearcher.songList.map {
                    if (it == null) {
                        Data.Error(Error("no internet"))
                    } else {
                        Data.Success(it.asPlaylist(query).songList)
                    }
                }
            )
            viewModelScope.launch {
                mIsInteractorBound.collect {
                    if (it) {
                        playerInteractor.isPlayerInitialised.collect { isInit ->
                            if (!isInit) {
                                songSearcher.songList.collect { songList ->
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
            songSearcher.searchSongs(query)
        }
    }

    class Factory @AssistedInject constructor(
        @Assisted("query") private val query: String,
        private val downloadInteractor: DownloadInteractor,
        private val storeInteractor: MediaStoreInteractor,
        private val songSearchersProvider: SongSearchersProvider
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RemoteSongsViewModel(
                query,
                songSearchersProvider,
                downloadInteractor,
                storeInteractor
            ) as T
        }

        @AssistedFactory
        interface RemoteAssistedFactory {
            fun create(@Assisted("query") query: String): Factory
        }
    }

    private fun List<RemoteSong>?.asPlaylist(query: String): PlaylistWrapper {
        return PlaylistWrapper(
            songList = this ?: emptyList(),
            name = query,
            id = -1
        )
    }
}
