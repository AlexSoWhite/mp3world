package com.nafanya.mp3world.model.wrappers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Playlist(
    @ColumnInfo(name = "songList") var songList: MutableList<Song>,
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "name") var name: String = ""
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
}
