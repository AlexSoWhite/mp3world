package com.nafanya.player

import android.graphics.Bitmap

/**
 * Class that wraps playlist.
 * @property id is maximum of ids of all playlists + 1
 */
data class Playlist(
    var songList: MutableList<Song>,
    val id: Int = 0,
    var name: String,
    var image: Bitmap? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this.id == (other as Playlist).id) return true
        return false
    }

    override fun hashCode(): Int {
        var result = songList.hashCode()
        result = 31 * result + id
        result = 31 * result + name.hashCode()
        return result
    }
}
