package com.nafanya.mp3world.features.allPlaylists.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.listUtils.StateMachine
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.features.allPlaylists.PlaylistListManager
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.ADD_PLAYLIST_BUTTON
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.AllPlaylistsListItem
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.PLAYLIST
import javax.inject.Inject
import kotlinx.coroutines.launch

class AllPlaylistsViewModel @Inject constructor(
    private val playlistListManager: PlaylistListManager
) : StateMachine<PlaylistWrapper, AllPlaylistsListItem>(
    playlistListManager.playlists.map { it }
),
    Searchable<PlaylistWrapper> {

    private var isModifyingPlaylistsTrigger = MutableLiveData(Unit)

    override val filter: QueryFilter<PlaylistWrapper> = QueryFilter { playlist, s ->
        playlist.name.contains(s, true)
    }

    init {
        addStateUpdateTrigger(isModifyingPlaylistsTrigger)
        applyFilter(this)
        resetInitialTitle("Мои плейлисты")
    }

    fun addEmptyPlaylistWithName(name: String) {
        isModifyingPlaylistsTrigger.value = Unit
        viewModelScope.launch {
            playlistListManager.addPlaylist(name)
        }
    }

    fun updatePlaylist(playlist: PlaylistWrapper) {
        isModifyingPlaylistsTrigger.value = Unit
        viewModelScope.launch {
            playlistListManager.updatePlaylist(playlist)
        }
    }

    fun deletePlaylist(playlist: PlaylistWrapper) {
        isModifyingPlaylistsTrigger.value = Unit
        viewModelScope.launch {
            playlistListManager.deletePlaylist(playlist)
        }
    }

    override fun List<PlaylistWrapper>.asListItems(): List<AllPlaylistsListItem> {
        val list = mutableListOf<AllPlaylistsListItem>()
        this.forEach { playlist ->
            list.add(AllPlaylistsListItem(PLAYLIST, playlist))
        }
        return if (list.isNotEmpty()) {
            listOf(AllPlaylistsListItem(ADD_PLAYLIST_BUTTON, Unit)) + list
        } else {
            listOf()
        }
    }
}
