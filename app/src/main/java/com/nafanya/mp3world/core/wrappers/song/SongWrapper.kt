package com.nafanya.mp3world.core.wrappers.song

import android.net.Uri
import com.nafanya.player.Song

abstract class SongWrapper(
    uri: Uri,
    val title: String,
    val artist: String,
    val duration: Long
) : Song(uri) {

    override fun equals(other: Any?): Boolean {
        return (this.uri == (other as? SongWrapper)?.uri)
    }

    override fun hashCode(): Int {
        var result = uri.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + duration.hashCode()
        return result
    }
}
