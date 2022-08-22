package com.nafanya.player

/**
 * Class that wraps playlist.
 * @property id is maximum of ids of all playlists + 1
 */
interface Playlist {
    val songList: List<Song>
}
