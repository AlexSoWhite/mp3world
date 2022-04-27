package com.nafanya.mp3world.model.dependencies

import com.nafanya.mp3world.viewmodel.listViewModels.playlists.PlaylistListViewModel

@Deprecated(message = "deprecated")
object PlaylistListViewModelProvider {

    private var playlistListViewModel: PlaylistListViewModel? = null

    fun putPlaylistListViewModel(playlistListViewModel: PlaylistListViewModel) {
        this.playlistListViewModel = playlistListViewModel
    }

    fun takePlaylistListViewModel(): PlaylistListViewModel? {
        val vm = this.playlistListViewModel
        this.playlistListViewModel = null
        return vm
    }
}
