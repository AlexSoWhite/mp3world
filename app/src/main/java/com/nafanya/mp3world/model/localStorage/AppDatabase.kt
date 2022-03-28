package com.nafanya.mp3world.model.localStorage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nafanya.mp3world.model.wrappers.FavouriteListEntity
import com.nafanya.mp3world.model.wrappers.PlaylistStorageEntity
import com.nafanya.mp3world.model.wrappers.SongStatisticEntity

@Database(
    entities = [
        PlaylistStorageEntity::class,
        FavouriteListEntity::class,
        SongStatisticEntity::class
    ],
    version = 8,
    exportSchema = true
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): StoredPlaylistDao
    abstract fun favouriteListDao(): FavouriteListDao
    abstract fun songStatisticDao(): SongStatisticDao
}
