package com.nafanya.mp3world.model.dependencies

import com.nafanya.mp3world.viewmodel.listViewModels.playlists.PlaylistViewModel

object PlaylistViewModelProvider {

    private var playlistViewModel: PlaylistViewModel? = null

    fun putPlaylistViewModel(playlistViewModel: PlaylistViewModel) {
        this.playlistViewModel = playlistViewModel
    }

    fun takePlaylistViewModel(): PlaylistViewModel? {
        val vm = this.playlistViewModel
        this.playlistViewModel = null
        return vm
    }
}
