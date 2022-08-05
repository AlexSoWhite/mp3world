package com.nafanya.mp3world.features.playlists.playlistsList.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.viewModel.ListViewModelInterface
import com.nafanya.mp3world.core.viewModel.PageState
import com.nafanya.mp3world.features.localStorage.LocalStorageProvider
import com.nafanya.mp3world.features.playlists.playlistsList.PlaylistListManager
import com.nafanya.player.PlayerInteractor
import com.nafanya.player.Playlist
import javax.inject.Inject
import kotlinx.coroutines.launch

class PlaylistListViewModel @Inject constructor(
    var localStorageProvider: LocalStorageProvider,
    private val playlistListManager: PlaylistListManager,
    playerInteractor: PlayerInteractor
) : ListViewModelInterface(playerInteractor) {

    private var query = ""

    private val mPlaylists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>>
        get() = mPlaylists

    fun addEmptyPlaylistWithName(name: String, callback: () -> Unit) {
        viewModelScope.launch {
            var id = 0
            playlistListManager.playlists.value?.forEach {
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
            playlistListManager.addPlaylist(playlist)
            // adding playlist to the local storage
            localStorageProvider.addPlaylist(playlist)
            callback()
            pageState.value = PageState.IS_LOADING
        }
    }

    override fun onLoading() {
        title.value = "Мои плейлисты"
        mPlaylists.value = playlistListManager.playlists.value
        if (playlists.value!!.isEmpty()) {
            pageState.value = PageState.IS_EMPTY
        } else {
            pageState.value = PageState.IS_LOADED
        }
    }

    override fun onLoaded() {
        if (query != "") {
            title.value = "$query (${playlistListManager.playlists.value?.size})"
        } else {
            title.value = "Мои плейлисты (${playlistListManager.playlists.value?.size})"
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
            val index = playlistListManager.playlists.value!!.indexOf(playlist)
            if (index != -1) {
                // modifying LiveData
                playlistListManager.updatePlaylist(playlist)
                // modifying playlist in local storage
                localStorageProvider.updatePlaylist(playlist, playlistListManager)
                mPlaylists.value = playlistListManager.playlists.value
                if (playlistListManager.playlists.value!!.isEmpty()) {
                    pageState.value = PageState.IS_EMPTY
                } else {
                    pageState.value = PageState.IS_LOADED
                }
            }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            val index = playlistListManager.playlists.value!!.indexOf(playlist)
            if (index != -1) {
                // modifying LiveData
                playlistListManager.deletePlaylist(playlist)
                // modifying playlist in local storage
                localStorageProvider.deletePlaylist(playlist)
                pageState.value = PageState.IS_LOADING
            }
        }
    }

    fun search(query: String) {
        val newList = mutableListOf<Playlist>()
        this.query = query
        playlistListManager.playlists.value!!.forEach {
            if (
                it.name.lowercase().contains(query.lowercase())
            ) {
                newList.add(it)
            }
        }
        mPlaylists.value = newList
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
