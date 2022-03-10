package com.nafanya.mp3world.viewmodel.listViewModels.playlists

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.localStorage.LocalStorageProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState

class PlaylistListViewModel : ListViewModelInterface() {

    val playlists: MutableLiveData<MutableList<Playlist>> by lazy {
        MutableLiveData<MutableList<Playlist>>()
    }

    fun addEmptyPlaylistWithName(context: Context, name: String, callback: () -> Unit) {
        val playlist = Playlist(
            songList = mutableListOf(),
            id = PlaylistListManager.playlists.value!!.size,
            name = name
        )
        // modifying LiveData
        PlaylistListManager.addPlaylist(playlist)
        // adding playlist to the local storage
        LocalStorageProvider.addPlaylist(context, playlist)
        callback()
        pageState.value = PageState.IS_LOADING
    }

//    fun subscribeToManager() {
//        val observer = Observer<MutableList<Playlist>> {
//            triggerTitle()
//        }
//    }

//    private fun triggerTitle() {
//        title.postValue("Мои плейлисты (${PlaylistListManager.playlists.value?.size})")
//    }

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

    fun updatePlaylist(context: Context, playlist: Playlist) {
        val index = PlaylistListManager.playlists.value!!.indexOf(playlist)
        if (index != -1) {
            // modifying LiveData
            PlaylistListManager.updatePlaylist(playlist)
            // modifying playlist in local storage
            LocalStorageProvider.updatePlaylist(context, playlist)
            playlists.value = PlaylistListManager.playlists.value
            if (PlaylistListManager.playlists.value!!.isEmpty()) {
                pageState.value = PageState.IS_EMPTY
            } else {
                pageState.value = PageState.IS_LOADED
            }
        }
    }
}
