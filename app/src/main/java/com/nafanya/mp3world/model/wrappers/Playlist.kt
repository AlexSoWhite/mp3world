package com.nafanya.mp3world.model.wrappers

data class Playlist(
    var songList: MutableList<Song>,
    val id: Int = 0,
    var name: String = ""
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
