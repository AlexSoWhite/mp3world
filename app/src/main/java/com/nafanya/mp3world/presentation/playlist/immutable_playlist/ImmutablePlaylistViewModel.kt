package com.nafanya.mp3world.presentation.playlist.immutable_playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.list_managers.PlaylistProviderMapWrapper
import com.nafanya.mp3world.core.state_machines.presentation.Data
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.utils.list_utils.searching.QueryFilter
import com.nafanya.mp3world.core.utils.list_utils.searching.SearchProcessor
import com.nafanya.mp3world.core.utils.list_utils.searching.Searchable
import com.nafanya.mp3world.core.utils.list_utils.searching.songQueryFilterCallback
import com.nafanya.mp3world.core.utils.list_utils.title.TitleModel
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessor
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessorWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.presentation.song_list_views.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.presentation.song_list_views.SongListItem
import com.nafanya.player.interactor.PlayerInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class ImmutablePlaylistViewModel(
    playlistProviderMapWrapper: PlaylistProviderMapWrapper,
    override val playerInteractor: PlayerInteractor,
    listManagerKey: Int,
    containerId: Long,
    playlistName: String?,
    playlistNameRes: Int?
) : StatedPlaylistViewModel(),
    Searchable<SongWrapper>,
    TitleProcessorWrapper<List<SongWrapper>> {

    override val playlistFlow = playlistProviderMapWrapper
        .getPlaylistProvider(listManagerKey)
        .getPlaylistByContainerId(containerId)

    private val searchProcessor = SearchProcessor(QueryFilter(songQueryFilterCallback))

    private val titleProcessor = if (playlistName != null) {
        TitleProcessor<List<SongWrapper>>(baseTitleString = playlistName)
    } else {
        TitleProcessor(baseTitleRes = playlistNameRes)
    }
    override val title: StateFlow<TitleModel>
        get() = titleProcessor.title

    init {
        model.load {
            titleProcessor.setup(model, viewModelScope)
            searchProcessor.setup(
                this@ImmutablePlaylistViewModel,
                playlistFlow.map {
                    Data.Success(it.songList)
                }
            )
        }
    }

    override fun asListItems(list: List<SongWrapper>): List<SongListItem> {
        return list.map { SongListItem(SONG_LOCAL_IMMUTABLE, it) }
    }

    override fun search(query: String) {
        searchProcessor.search(query)
    }

    class Factory @AssistedInject constructor(
        @Assisted("listManagerKey") private val listManagerKey: Int,
        @Assisted("containerId") private val containerId: Long,
        @Assisted("playlistName") private val playlistName: String?,
        @Assisted("playlistNameRes") private val playlistNameRes: Int?,
        private val playlistProviderMapWrapper: PlaylistProviderMapWrapper,
        private val playerInteractor: PlayerInteractor
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return ImmutablePlaylistViewModel(
                playlistProviderMapWrapper,
                playerInteractor,
                listManagerKey,
                containerId,
                playlistName,
                playlistNameRes
            ) as T
        }

        @AssistedFactory
        interface AssistedPlaylistFactory {

            fun create(
                @Assisted("listManagerKey") listManagerKey: Int,
                @Assisted("containerId") containerId: Long,
                @Assisted("playlistName") playlistName: String?,
                @Assisted("playlistNameRes") playlistNameRes: Int?
            ): Factory
        }
    }
}
