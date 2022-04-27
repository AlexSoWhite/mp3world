package com.nafanya.mp3world.model.wrappers

import android.graphics.Bitmap

/**
 * Class that wraps the album.
 * @property id is taken from MediaStore.
 */
data class Album(
    val name: String,
    val id: Long,
    var playlist: Playlist? = null,
    var image: Bitmap?
) {
    override fun equals(other: Any?): Boolean {
        if (this.id == (other as Album).id) return true
        return false
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + playlist.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}
