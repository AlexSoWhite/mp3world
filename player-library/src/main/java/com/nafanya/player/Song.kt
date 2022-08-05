package com.nafanya.player

import android.graphics.Bitmap

/**
 * Class that wraps song.
 * @property id is taken from MediaStore.
 */
data class Song(
    val id: Long,
    val title: String,
    val artistId: Long,
    val artist: String,
    val albumId: Long,
    val album: String,
    val date: Long?,
    val url: String?,
    val duration: Long,
    val art: Bitmap?,
    val artUrl: String?
) {

    override fun equals(other: Any?): Boolean {
        if (this.id == (other as Song).id && this.url == (other).url) {
            return true
        }
        return false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artistId.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + albumId.hashCode()
        result = 31 * result + album.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + art.hashCode()
        result = 31 * result + artUrl.hashCode()
        return result
    }
}
