package com.nafanya.mp3world.domain.albums

import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper

/**
 * Class that wraps the album.
 * @property id is taken from MediaStore.
 */
data class Album(
    val name: String,
    val id: Long,
    var playlist: PlaylistWrapper? = null,
    var imageSource: SongWrapper?
) {
    override fun equals(other: Any?): Boolean {
        return this.id == (other as Album).id
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + playlist.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}
