package com.nafanya.mp3world.features.remoteSongs.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.coroutines.collectInScope
import com.nafanya.mp3world.core.stateMachines.commonUi.Data
import com.nafanya.mp3world.core.stateMachines.commonUi.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.utils.listUtils.title.TitleProcessor
import com.nafanya.mp3world.core.utils.listUtils.title.TitleProcessorWrapper
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.features.downloading.api.DownloadInteractor
import com.nafanya.mp3world.features.downloading.api.DownloadResult
import com.nafanya.mp3world.features.downloading.api.DownloadingViewModel
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import com.nafanya.mp3world.features.remoteSongs.songSearchers.HITMO_TOP
import com.nafanya.mp3world.features.remoteSongs.songSearchers.SongSearcher
import com.nafanya.mp3world.features.remoteSongs.songSearchers.SongSearchersProvider
import com.nafanya.mp3world.features.songListViews.SONG_REMOTE
import com.nafanya.mp3world.features.songListViews.SongListItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RemoteSongsViewModel(
    private val query: String,
    private val songSearchersProvider: SongSearchersProvider,
    private val ioCoroutineProvider: IOCoroutineProvider,
    private val downloadInteractor: DownloadInteractor,
    private val mediaStoreInteractor: MediaStoreInteractor
) : StatedPlaylistViewModel(),
    DownloadingViewModel,
    TitleProcessorWrapper<List<SongWrapper>> {

    private val songSearcher: SongSearcher
        get() = songSearchersProvider.getSongSearcher(HITMO_TOP)

    private val mPlaylistFlow = MutableSharedFlow<List<RemoteSong>?>(replay = 1)
    override val playlistFlow: Flow<PlaylistWrapper>
        get() = mPlaylistFlow.map {
            it.asPlaylist(query)
        }

    private val titleProcessor = TitleProcessor<List<SongWrapper>>()

    override val title: LiveData<String>
        get() = titleProcessor.title

    init {
        model.load {
            titleProcessor.setup(this.model, viewModelScope)
            titleProcessor.setBaseTitle(query)
            ioCoroutineProvider.ioScope.launch {
                songSearcher.searchSongs(query) {
                    viewModelScope.launch { mPlaylistFlow.emit(it) }
                }
            }
            setDataSource(
                mPlaylistFlow.map {
                    if (it == null) {
                        Data.Error(Error("no internet"))
                    } else {
                        Data.Success(it.asPlaylist(query).songList)
                    }
                }
            )
            mIsInteractorBound.collectInScope(viewModelScope) {
                if (it) {
                    playerInteractor.isPlayerInitialised.collectInScope(viewModelScope) { isInit ->
                        if (!isInit) {
                            mPlaylistFlow.collectInScope(viewModelScope) { songList ->
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

    override fun asListItems(list: List<SongWrapper>): List<SongListItem> {
        return list.map { SongListItem(SONG_REMOTE, it) }
    }

    fun refresh() {
        model.refresh {
            ioCoroutineProvider.ioScope.launch {
                songSearcher.searchSongs(query) {
                    viewModelScope.launch { mPlaylistFlow.emit(it) }
                }
            }
        }
    }

    override fun download(remoteSong: RemoteSong, callback: (DownloadResult) -> Unit) {
        downloadInteractor.download(remoteSong, callback)
    }

    override fun resetMediaStore() {
        mediaStoreInteractor.reset()
    }

    class Factory @AssistedInject constructor(
        @Assisted("query") private val query: String,
        private val downloadInteractor: DownloadInteractor,
        private val ioCoroutineProvider: IOCoroutineProvider,
        private val mediaStoreInteractor: MediaStoreInteractor,
        private val songSearchersProvider: SongSearchersProvider
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RemoteSongsViewModel(
                query,
                songSearchersProvider,
                ioCoroutineProvider,
                downloadInteractor,
                mediaStoreInteractor
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
