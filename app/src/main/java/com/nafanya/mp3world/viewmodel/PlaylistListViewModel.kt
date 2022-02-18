package com.nafanya.mp3world.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.wrappers.Playlist
import kotlinx.coroutines.launch

class PlaylistListViewModel : ViewModel() {

    fun addEmptyPlaylistWithName(name: String) {
        viewModelScope.launch {
            PlaylistListManager.addPlaylist(
                Playlist(
                    ArrayList(),
                    PlaylistListManager.playlists.size,
                    name
                )
            )
        }
    }
}
