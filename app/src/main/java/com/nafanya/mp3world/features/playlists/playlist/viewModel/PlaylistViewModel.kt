package com.nafanya.mp3world.features.playlists.playlist.viewModel

import com.nafanya.mp3world.features.allSongs.SongListViewModel
import com.nafanya.mp3world.features.playlists.playlist.Playlist
import com.nafanya.mp3world.features.playlists.playlistsList.PlaylistListViewModelProvider
import javax.inject.Inject

class PlaylistViewModel @Inject constructor(
    initializingPlaylist: Playlist
) : SongListViewModel(initializingPlaylist) {

    fun resetPlaylist(playlist: Playlist) {
        updateData(playlist)
        PlaylistListViewModelProvider.takePlaylistListViewModel()?.updatePlaylist(playlist)
    }
}
