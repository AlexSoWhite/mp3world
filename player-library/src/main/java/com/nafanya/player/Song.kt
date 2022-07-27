package com.nafanya.player

import android.graphics.Bitmap

/**
 * Class that wraps song.
 * @property id is taken from MediaStore.
 */
data class Song(
    var id: Long = 0,
    var title: String? = null,
    var artist: String? = null,
    var date: Long? = null,
    var url: String? = null,
    var duration: Long? = null,
    val art: Bitmap? = null,
    val artUrl: String? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this.id == (other as Song).id && this.url == (other as Song).url) {
            return true
        }
        return false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (artist?.hashCode() ?: 0)
        result = 31 * result + (date?.hashCode() ?: 0)
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + (duration?.hashCode() ?: 0)
        result = 31 * result + (art?.hashCode() ?: 0)
        result = 31 * result + (artUrl?.hashCode() ?: 0)
        return result
    }
}
