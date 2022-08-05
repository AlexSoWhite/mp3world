package com.nafanya.mp3world.features.playlists.playlist.viewModel

import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.mp3world.features.allSongs.SongListViewModel
import com.nafanya.mp3world.features.playlists.playlistsList.PlaylistListViewModelProvider
import com.nafanya.player.PlayerInteractor
import com.nafanya.player.Playlist
import javax.inject.Inject

class PlaylistViewModel @Inject constructor(
    initializingPlaylist: Playlist,
    playerInteractor: PlayerInteractor,
    songListManager: SongListManager
) : SongListViewModel(initializingPlaylist, songListManager, playerInteractor) {

    fun resetPlaylist(playlist: Playlist) {
        updateData(playlist)
        PlaylistListViewModelProvider.takePlaylistListViewModel()?.updatePlaylist(playlist)
    }
}
