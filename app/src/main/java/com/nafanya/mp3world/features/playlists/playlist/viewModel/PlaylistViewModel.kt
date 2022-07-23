package com.nafanya.mp3world.features.playlists.playlist.viewModel

import com.nafanya.mp3world.features.playlists.playlistsList.model.PlaylistListViewModelProvider
import com.nafanya.mp3world.features.playlists.playlist.model.Playlist
import com.nafanya.mp3world.features.allSongs.SongListViewModel
import javax.inject.Inject

class PlaylistViewModel @Inject constructor(
    initializingPlaylist: Playlist
) : SongListViewModel(initializingPlaylist) {

    fun resetPlaylist(playlist: Playlist) {
        updateData(playlist)
        PlaylistListViewModelProvider.takePlaylistListViewModel()?.updatePlaylist(playlist)
    }
}
