package com.nafanya.mp3world.presentation.remote_songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.state_machines.presentation.Data
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.utils.list_utils.title.TitleModel
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessor
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessorWrapper
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.data.downloading.api.DownloadInteractor
import com.nafanya.mp3world.data.downloading.api.DownloadingViewModel
import com.nafanya.mp3world.data.media_store.MediaStoreInteractor
import com.nafanya.mp3world.data.remote_songs.HITMO_TOP
import com.nafanya.mp3world.data.remote_songs.SongSearcher
import com.nafanya.mp3world.data.remote_songs.SongSearchersProvider
import com.nafanya.mp3world.presentation.song_list_views.SONG_REMOTE
import com.nafanya.mp3world.presentation.song_list_views.SongListItem
import com.nafanya.player.interactor.PlayerInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class RemoteSongsViewModel(
    private val query: String,
    private val songSearchersProvider: SongSearchersProvider,
    private val downloadInteractor: DownloadInteractor,
    private val mediaStoreInteractor: MediaStoreInteractor,
    override val playerInteractor: PlayerInteractor
) : StatedPlaylistViewModel(),
    DownloadingViewModel,
    TitleProcessorWrapper<List<SongWrapper>> {

    private val songSearcher: SongSearcher
        get() = songSearchersProvider.getSongSearcher(HITMO_TOP) // todo: unite with others just in case?

    private val mPlaylistFlow = MutableSharedFlow<List<RemoteSong>?>(replay = 1)
    override val playlistFlow: Flow<PlaylistWrapper>
        get() = mPlaylistFlow.map {
            it.asPlaylist(query)
        }

    private val titleProcessor = TitleProcessor<List<SongWrapper>>(baseTitleString = query)

    override val title: StateFlow<TitleModel>
        get() = titleProcessor.title

    init {
        model.load {
            titleProcessor.setup(this.model, viewModelScope)
            viewModelScope.launch {
                val songs = songSearcher.searchSongs(query)
                mPlaylistFlow.emit(songs)
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
            viewModelScope.launch {
                // set initial playlist using remote songs if there are no songs on device
                combine(
                    playerInteractor.isPlayerPresent,
                    playerInteractor.isPlayerReady,
                    mPlaylistFlow.filter { it?.isNotEmpty() == true }.take(1)
                ) { isPresent, isPresentAndSongSubmitted, songList ->
                    if (isPresent && !isPresentAndSongSubmitted) {
                        playerInteractor.setPlaylist(songList.asPlaylist(query))
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
            viewModelScope.launch {
                val songs = songSearcher.searchSongs(query)
                mPlaylistFlow.emit(songs)
            }
        }
    }

    override fun download(remoteSong: RemoteSong) = downloadInteractor.download(remoteSong)

    class Factory @AssistedInject constructor(
        @Assisted("query") private val query: String,
        private val downloadInteractor: DownloadInteractor,
        private val mediaStoreInteractor: MediaStoreInteractor,
        private val songSearchersProvider: SongSearchersProvider,
        private val playerInteractor: PlayerInteractor
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RemoteSongsViewModel(
                query,
                songSearchersProvider,
                downloadInteractor,
                mediaStoreInteractor,
                playerInteractor
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
