package com.nafanya.mp3world.features.allPlaylists.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.listUtils.searching.SearchProcessor
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.core.listUtils.title.TitleProcessor
import com.nafanya.mp3world.core.listUtils.title.TitleViewModel
import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.stateMachines.list.StatedListViewModel
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.features.allPlaylists.PlaylistListManager
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.ADD_PLAYLIST_BUTTON
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.AllPlaylistsListItem
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.PLAYLIST
import javax.inject.Inject
import kotlinx.coroutines.launch

class AllPlaylistsViewModel @Inject constructor(
    private val playlistListManager: PlaylistListManager,
) : StatedListViewModel<PlaylistWrapper, AllPlaylistsListItem>(),
    Searchable<PlaylistWrapper>,
    TitleViewModel<List<PlaylistWrapper>> {

    private var isModifyingPlaylistsTrigger = MutableLiveData(Unit)

    private val searchProcessor = SearchProcessor<PlaylistWrapper>(
        QueryFilter { playlist, s ->
            playlist.name.contains(s, true)
        }
    )

    private val titleProcessor = TitleProcessor<List<PlaylistWrapper>>()
    override val title: LiveData<String>
        get() = titleProcessor.title

    init {
        model.load {
            titleProcessor.setup(model, viewModelScope)
            // TODO string resource
            titleProcessor.setBaseTitle("Мои плейлисты")
            searchProcessor.setup(
                this@AllPlaylistsViewModel,
                playlistListManager.playlists.map { Data.Success(it) }.asFlow()
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
