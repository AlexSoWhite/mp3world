package com.nafanya.mp3world.features.artists

import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper

/**
 * Class that wraps the artist.
 * @property id is taken from MediaStore.
 */
data class Artist(
    val name: String,
    val id: Long,
    var playlist: PlaylistWrapper? = null,
    var imageSource: SongWrapper?
) {
    override fun equals(other: Any?): Boolean {
        if (this.id == (other as Artist).id) return true
        return false
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + playlist.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}
