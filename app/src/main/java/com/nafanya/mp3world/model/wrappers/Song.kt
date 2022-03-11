package com.nafanya.mp3world.model.wrappers

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Song(
    @PrimaryKey var id: Long = 0,
    @ColumnInfo(name = "title") var title: String? = null,
    @ColumnInfo(name = "artist") var artist: String? = null,
    @ColumnInfo(name = "date") var date: String? = null,
    @ColumnInfo(name = "url") var url: String? = null,
    @ColumnInfo(name = "duration") var duration: Int? = null,
    @ColumnInfo(name = "path") var path: String? = null,
    @Ignore
    @ColumnInfo(name = "art") val art: Bitmap? = null
)
