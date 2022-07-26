package com.nafanya.mp3world.features.playlists.playlist.viewModel

import com.nafanya.mp3world.features.allSongs.SongListViewModel
import com.nafanya.player.Playlist
import com.nafanya.mp3world.features.playlists.playlistsList.PlaylistListViewModelProvider
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject

class PlaylistViewModel @Inject constructor(
    initializingPlaylist: Playlist,
    playerInteractor: PlayerInteractor
) : SongListViewModel(initializingPlaylist, playerInteractor) {

    fun resetPlaylist(playlist: Playlist) {
        updateData(playlist)
        PlaylistListViewModelProvider.takePlaylistListViewModel()?.updatePlaylist(playlist)
    }
}
