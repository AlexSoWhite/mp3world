package com.nafanya.mp3world.core.wrappers

import android.graphics.Bitmap
import com.nafanya.player.Playlist

data class PlaylistWrapper(
    override val songList: List<SongWrapper>,
    val id: Long = 0,
    val name: String,
    val position: Int = 0,
    var image: Bitmap? = null
) : Playlist {

    override fun equals(other: Any?): Boolean {
        return this.id == (other as PlaylistWrapper).id
    }

    override fun hashCode(): Int {
        var result = songList.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + position
        return result
    }
}
