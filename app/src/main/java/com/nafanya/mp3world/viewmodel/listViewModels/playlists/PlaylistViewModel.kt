package com.nafanya.mp3world.viewmodel.listViewModels.playlists

import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    initializingPlaylist: Playlist
) : SongListViewModel(initializingPlaylist) {

    fun resetPlaylist(playlist: Playlist) {
        updateData(playlist)
    }
}
