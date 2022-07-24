package com.nafanya.mp3world.features.playlists.playlistsList

import com.nafanya.mp3world.features.playlists.playlistsList.viewModel.PlaylistListViewModel

// TODO wtf is this object I have to use multibindings
object PlaylistListViewModelProvider {

    private var playlistListViewModel: PlaylistListViewModel? = null

    fun putPlaylistListViewModel(playlistListViewModel: PlaylistListViewModel) {
        PlaylistListViewModelProvider.playlistListViewModel = playlistListViewModel
    }

    fun takePlaylistListViewModel(): PlaylistListViewModel? {
        val vm = playlistListViewModel
        playlistListViewModel = null
        return vm
    }
}
