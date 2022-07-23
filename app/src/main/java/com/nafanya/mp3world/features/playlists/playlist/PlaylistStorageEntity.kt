package com.nafanya.mp3world.features.playlists.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaylistStorageEntity(
    @ColumnInfo(name = "songIds") var songIds: MutableList<Long>?,
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "name") var name: String = ""
)
