package com.nafanya.mp3world.viewmodel.listViewModels.playlists

import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel

class PlaylistViewModel : SongListViewModel() {

    fun resetPlaylist(playlist: Playlist) {
        updateData(playlist)
        parentViewModel.updatePlaylist(playlist)
    }

    companion object {

        private lateinit var parentViewModel: PlaylistListViewModel

        fun newInstance(parentViewModel: PlaylistListViewModel) {
            this.parentViewModel = parentViewModel
        }
    }
}
