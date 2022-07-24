package com.nafanya.mp3world.features.statistics

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SongStatisticEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "time") var time: Long? = null,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "artist") val artist: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this.id == (other as SongStatisticEntity).id) return true
        return false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artist.hashCode()
        return result
    }
}
