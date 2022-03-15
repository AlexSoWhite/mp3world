package com.nafanya.mp3world.model.localStorage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nafanya.mp3world.model.wrappers.FavouriteListEntity
import com.nafanya.mp3world.model.wrappers.PlaylistStorageEntity
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.model.wrappers.SongStatisticEntity

@Database(
    entities = [
        PlaylistStorageEntity::class,
        Song::class,
        FavouriteListEntity::class,
        SongStatisticEntity::class
    ],
    version = 6,
    exportSchema = true
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): StoredPlaylistDao
    abstract fun songsListDao(): SongDao
    abstract fun favouriteListDao(): FavouriteListDao
    abstract fun songStatisticDao(): SongStatisticDao
}
