package com.nafanya.mp3world.viewmodel.listViewModels.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.wrappers.Playlist
import kotlinx.coroutines.launch

class PlaylistListViewModel : ViewModel() {

    fun addEmptyPlaylistWithName(name: String, callback: (Playlist) -> Unit) {
        viewModelScope.launch {
            val playlist = Playlist(
                ArrayList(),
                PlaylistListManager.playlists.size,
                name
            )
            PlaylistListManager.addPlaylist(
                playlist
            )
            callback(playlist)
        }
    }
}
