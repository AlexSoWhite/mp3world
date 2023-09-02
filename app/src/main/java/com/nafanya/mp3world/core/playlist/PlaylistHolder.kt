package com.nafanya.mp3world.core.playlist

import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter

interface PlaylistHolder {

    val playlistViewModel: StatedPlaylistViewModel
    val songListAdapter: BaseSongListAdapter
}
