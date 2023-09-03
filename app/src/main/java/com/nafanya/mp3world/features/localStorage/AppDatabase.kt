package com.nafanya.mp3world.features.localStorage

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nafanya.mp3world.features.allPlaylists.model.PlaylistSongsEntity
import com.nafanya.mp3world.features.allPlaylists.model.PlaylistStorageEntity
import com.nafanya.mp3world.features.favorites.FavouriteListEntity

@Database(
    entities = [
        PlaylistStorageEntity::class,
        PlaylistSongsEntity::class,
        FavouriteListEntity::class
    ],
    version = 13,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 10, to = 11),
        AutoMigration(from = 11, to = 12),
        AutoMigration(from = 12, to = 13)
    ]
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun allPlaylistsListDao(): AllPlaylistsListDao
    abstract fun favouriteListDao(): FavouriteListDao
}
