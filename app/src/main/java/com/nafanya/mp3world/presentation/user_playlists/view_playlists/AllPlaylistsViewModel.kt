package com.nafanya.mp3world.presentation.user_playlists.view_playlists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.state_machines.presentation.Data
import com.nafanya.mp3world.core.state_machines.presentation.list.StatedListViewModel
import com.nafanya.mp3world.core.utils.list_utils.searching.QueryFilter
import com.nafanya.mp3world.core.utils.list_utils.searching.SearchProcessor
import com.nafanya.mp3world.core.utils.list_utils.searching.Searchable
import com.nafanya.mp3world.core.utils.list_utils.title.TitleModel
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessor
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessorWrapper
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.domain.user_playlists.UserPlaylistsInteractor
import com.nafanya.mp3world.presentation.user_playlists.view_playlists.recycler.ADD_PLAYLIST_BUTTON
import com.nafanya.mp3world.presentation.user_playlists.view_playlists.recycler.AllPlaylistsListItem
import com.nafanya.mp3world.presentation.user_playlists.view_playlists.recycler.PLAYLIST
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AllPlaylistsViewModel @Inject constructor(
    private val playlistListManager: UserPlaylistsInteractor,
) : StatedListViewModel<PlaylistWrapper, AllPlaylistsListItem>(),
    Searchable<PlaylistWrapper>,
    TitleProcessorWrapper<List<PlaylistWrapper>> {

    private var isModifyingPlaylistsTrigger = MutableLiveData(Unit)

    private val searchProcessor = SearchProcessor<PlaylistWrapper>(
        QueryFilter { playlist, s ->
            playlist.name.contains(s, true)
        }
    )

    private val titleProcessor = TitleProcessor<List<PlaylistWrapper>>(
        baseTitleRes = R.string.my_playlists
    )
    override val title: StateFlow<TitleModel>
        get() = titleProcessor.title

    init {
        model.load {
            titleProcessor.setup(model, viewModelScope)
            searchProcessor.setup(
                this,
                playlistListManager.playlists.map { Data.Success(it) }
            )
        }
    }

    fun addEmptyPlaylistWithName(name: String) {
        isModifyingPlaylistsTrigger.value = Unit
        viewModelScope.launch {
            playlistListManager.addPlaylist(name)
        }
    }

    fun deletePlaylist(playlist: PlaylistWrapper) {
        isModifyingPlaylistsTrigger.value = Unit
        viewModelScope.launch {
            playlistListManager.deletePlaylist(playlist)
        }
    }

    override fun asListItems(list: List<PlaylistWrapper>): List<AllPlaylistsListItem> {
        val itemsList = mutableListOf<AllPlaylistsListItem>()
        list.forEach { playlist ->
            itemsList.add(AllPlaylistsListItem(PLAYLIST, playlist))
        }
        return if (itemsList.isNotEmpty()) {
            listOf(AllPlaylistsListItem(ADD_PLAYLIST_BUTTON, Unit)) + itemsList
        } else {
            listOf()
        }
    }

    override fun search(query: String) {
        searchProcessor.search(query)
    }
}
