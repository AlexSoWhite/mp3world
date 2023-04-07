package com.nafanya.mp3world.features.allPlaylists.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.listUtils.searching.SearchableStated
import com.nafanya.mp3world.core.listUtils.searching.StatedQueryFilter
import com.nafanya.mp3world.core.stateMachines.State
import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.stateMachines.list.StatedListViewModel
import com.nafanya.mp3world.core.stateMachines.title.TitleViewModel
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.features.allPlaylists.PlaylistListManager
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.ADD_PLAYLIST_BUTTON
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.AllPlaylistsListItem
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.PLAYLIST
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AllPlaylistsViewModel @Inject constructor(
    private val playlistListManager: PlaylistListManager,
) : StatedListViewModel<PlaylistWrapper, AllPlaylistsListItem>(),
    SearchableStated<PlaylistWrapper>,
    TitleViewModel<List<PlaylistWrapper>> {

    private var isModifyingPlaylistsTrigger = MutableLiveData(Unit)

    override val queryFilter: StatedQueryFilter<PlaylistWrapper> =
        StatedQueryFilter { playlist, s ->
            playlist.name.contains(s, true)
        }

    override val baseTitle = "Мои плейлисты"
    override val mTitle = MutableStateFlow(baseTitle)

    override val stateMapper: (
        suspend (State<List<PlaylistWrapper>>) -> State<List<PlaylistWrapper>>
    )? = null

    init {
        model.load {
            viewModelScope.launch {
                setDataSourceFiltered(
                    playlistListManager.playlists.asFlow().map {
                        Data.Success(it)
                    }
                )
                model.startListeningModelForTitle()
            }
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
}
