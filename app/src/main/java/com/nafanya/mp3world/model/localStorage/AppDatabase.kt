package com.nafanya.mp3world.model.localStorage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nafanya.mp3world.model.wrappers.PlaylistStorageEntity
import com.nafanya.mp3world.model.wrappers.Song

@Database(
    entities = [PlaylistStorageEntity::class, Song::class],
    version = 4,
    exportSchema = true
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): StoredPlaylistDao
    abstract fun songsListDao(): SongDao
}
