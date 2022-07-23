package com.nafanya.mp3world.features.playlists.playlist.model

import android.graphics.Bitmap
import com.nafanya.mp3world.core.domain.Song

/**
 * Class that wraps playlist.
 * @property id is maximum of ids of all playlists + 1
 */
data class Playlist(
    var songList: MutableList<Song>,
    val id: Int = 0,
    var name: String = "Мои песни",
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

    fun toStorageEntity(): PlaylistStorageEntity {
        val songIds = mutableListOf<Long>()
        songList.forEach {
            songIds.add(it.id)
        }
        return PlaylistStorageEntity(songIds, id, name)
    }
}
