package com.nafanya.mp3world.model.wrappers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Playlist(
    @ColumnInfo(name = "songList") var songList: ArrayList<Song>,
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "name") var name: String = ""
)
