package com.nafanya.mp3world.viewmodel.listViewModels.playlists

import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.viewmodel.listViewModels.ListViewModelInterface
import com.nafanya.mp3world.viewmodel.listViewModels.PageState
import kotlinx.coroutines.launch

class PlaylistListViewModel : ListViewModelInterface() {

    fun addEmptyPlaylistWithName(name: String, callback: (Playlist) -> Unit) {
        viewModelScope.launch {
            val playlist = Playlist(
                ArrayList(),
                PlaylistListManager.playlists.value!!.size,
                name
            )
            PlaylistListManager.addPlaylist(
                playlist
            )
            triggerTitle()
            callback(playlist)
        }
    }

    fun getData(callback: (MutableList<Playlist>?) -> Unit) {
        triggerTitle()
        pageState.value = PageState.IS_LOADED
        callback(PlaylistListManager.playlists.value)
    }

    fun subscribeToManager() {
        val observer = Observer<MutableList<Playlist>> {
            triggerTitle()
        }
    }

    private fun triggerTitle() {
        title.value = "Мои плейлисты (${PlaylistListManager.playlists.value?.size})"
    }
}
