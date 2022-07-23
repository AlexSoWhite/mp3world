package com.nafanya.mp3world.features.playlists.playlist

import com.nafanya.mp3world.features.playlists.playlist.viewModel.PlaylistViewModel

// TODO need to refactor it too
object PlaylistViewModelProvider {

    private var playlistViewModel: PlaylistViewModel? = null

    fun putPlaylistViewModel(playlistViewModel: PlaylistViewModel) {
        PlaylistViewModelProvider.playlistViewModel = playlistViewModel
    }

    fun takePlaylistViewModel(): PlaylistViewModel? {
        val vm = playlistViewModel
        playlistViewModel = null
        return vm
    }
}
