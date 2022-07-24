package com.nafanya.mp3world.features.playlists.playlistsList.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.viewModel.ListViewModelInterface
import com.nafanya.mp3world.core.viewModel.PageState
import com.nafanya.mp3world.features.localStorage.LocalStorageProvider
import com.nafanya.mp3world.features.playlists.playlist.Playlist
import com.nafanya.mp3world.features.playlists.playlistsList.PlaylistListManager
import javax.inject.Inject
import kotlinx.coroutines.launch

class PlaylistListViewModel @Inject constructor(
    var localStorageProvider: LocalStorageProvider
) : ListViewModelInterface() {

    private var query = ""

    val playlists: MutableLiveData<MutableList<Playlist>> by lazy {
        MutableLiveData<MutableList<Playlist>>()
    }

    fun addEmptyPlaylistWithName(name: String, callback: () -> Unit) {
        viewModelScope.launch {
            var id = 0
            PlaylistListManager.playlists.value?.forEach {
                if (id < it.id) {
                    id = it.id
                }
            }
            id++
            val playlist = Playlist(
                songList = mutableListOf(),
                id = id,
                name = name
            )
            // modifying LiveData
            PlaylistListManager.addPlaylist(playlist)
            // adding playlist to the local storage
            localStorageProvider.addPlaylist(playlist)
            callback()
            pageState.value = PageState.IS_LOADING
        }
    }

    override fun onLoading() {
        title.value = "Мои плейлисты"
        playlists.value = PlaylistListManager.playlists.value
        if (playlists.value!!.isEmpty()) {
            pageState.value = PageState.IS_EMPTY
        } else {
            pageState.value = PageState.IS_LOADED
        }
    }

    override fun onLoaded() {
        if (query != "") {
            title.value = "$query (${PlaylistListManager.playlists.value?.size})"
        } else {
            title.value = "Мои плейлисты (${PlaylistListManager.playlists.value?.size})"
        }
    }

    override fun onEmpty() {
        if (query != "") {
            title.value = "Мои плейлисты"
        } else {
            title.value = query
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            val index = PlaylistListManager.playlists.value!!.indexOf(playlist)
            if (index != -1) {
                // modifying LiveData
                PlaylistListManager.updatePlaylist(playlist)
                // modifying playlist in local storage
                localStorageProvider.updatePlaylist(playlist)
                playlists.value = PlaylistListManager.playlists.value
                if (PlaylistListManager.playlists.value!!.isEmpty()) {
                    pageState.value = PageState.IS_EMPTY
                } else {
                    pageState.value = PageState.IS_LOADED
                }
            }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            val index = PlaylistListManager.playlists.value!!.indexOf(playlist)
            if (index != -1) {
                // modifying LiveData
                PlaylistListManager.deletePlaylist(playlist)
                // modifying playlist in local storage
                localStorageProvider.deletePlaylist(playlist)
                pageState.value = PageState.IS_LOADING
            }
        }
    }

    fun search(query: String) {
        val newList = mutableListOf<Playlist>()
        this.query = query
        PlaylistListManager.playlists.value!!.forEach {
            if (
                it.name.lowercase().contains(query.lowercase())
            ) {
                newList.add(it)
            }
        }
        playlists.value = newList
        if (playlists.value!!.isEmpty()) {
            pageState.value = PageState.IS_EMPTY
        } else {
            pageState.value = PageState.IS_LOADED
        }
    }

    fun reset() {
        query = ""
        pageState.value = PageState.IS_LOADING
    }
}
