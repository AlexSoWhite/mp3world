package com.nafanya.mp3world.viewmodel.listViewModels.playlists

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState

class PlaylistListViewModel : ListViewModelInterface() {

    val playlists: MutableLiveData<MutableList<Playlist>> by lazy {
        MutableLiveData<MutableList<Playlist>>()
    }

    fun addEmptyPlaylistWithName(name: String, callback: () -> Unit) {
        val playlist = Playlist(
            mutableListOf(),
            PlaylistListManager.playlists.value!!.size,
            name
        )
        PlaylistListManager.addPlaylist(playlist)
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

    }
}
