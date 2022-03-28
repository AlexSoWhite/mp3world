package com.nafanya.mp3world.viewmodel.listViewModels.playlists

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.localStorage.LocalStorageProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class PlaylistListViewModel : ListViewModelInterface() {

    fun addEmptyPlaylistWithName(name: String, callback: () -> Unit) {
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
        LocalStorageProvider().addPlaylist(playlist)
        callback()
        pageState.value = PageState.IS_LOADING
    }

    val playlists: MutableLiveData<MutableList<Playlist>> by lazy {
        MutableLiveData<MutableList<Playlist>>()
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
        title.postValue("Мои плейлисты (${PlaylistListManager.playlists.value?.size})")
    }

    override fun onEmpty() {
        title.value = "Мои плейлисты"
    }

    fun updatePlaylist(playlist: Playlist) {
        val index = PlaylistListManager.playlists.value!!.indexOf(playlist)
        if (index != -1) {
            // modifying LiveData
            PlaylistListManager.updatePlaylist(playlist)
            // modifying playlist in local storage
            LocalStorageProvider().updatePlaylist(playlist)
            playlists.value = PlaylistListManager.playlists.value
            if (PlaylistListManager.playlists.value!!.isEmpty()) {
                pageState.value = PageState.IS_EMPTY
            } else {
                pageState.value = PageState.IS_LOADED
            }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        val index = PlaylistListManager.playlists.value!!.indexOf(playlist)
        if (index != -1) {
            // modifying LiveData
            PlaylistListManager.deletePlaylist(playlist)
            // modifying playlist in local storage
            LocalStorageProvider().deletePlaylist(playlist)
            pageState.value = PageState.IS_LOADING
        }
    }
}
