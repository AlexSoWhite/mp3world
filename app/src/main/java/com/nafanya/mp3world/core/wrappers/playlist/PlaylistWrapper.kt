package com.nafanya.mp3world.core.wrappers.playlist

import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.player.Playlist

data class PlaylistWrapper(
    override val songList: List<SongWrapper>,
    val id: Long = 0,
    val name: String,
    val position: Int = 0, // for local db
    var imageSource: SongWrapper? = null
) : Playlist {

    override fun equals(other: Any?): Boolean {
        return this.id == (other as? PlaylistWrapper)?.id &&
            this.name == other.name
    }

    override fun hashCode(): Int {
        var result = songList.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + imageSource.hashCode()
        result = 31 * result + position
        return result
    }

    override fun areSongListsTheSame(other: Playlist): Boolean = songList == other.songList

    override fun toString(): String {
        return "PlaylistWrapper(id=$id, name=$name, songsCount=${songList.size}, songListHash=${songList.hashCode()})"
    }
}
