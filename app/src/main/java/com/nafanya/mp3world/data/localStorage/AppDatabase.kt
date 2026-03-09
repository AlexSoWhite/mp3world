package com.nafanya.mp3world.data.localStorage

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nafanya.mp3world.features.userPlaylists.data.PlaylistSongsEntity
import com.nafanya.mp3world.features.userPlaylists.data.PlaylistStorageEntity
import com.nafanya.mp3world.features.favourites.data.FavouritesEntity
import com.nafanya.mp3world.data.localStorage.migrations.AutoMigration1314

@Database(
    entities = [
        PlaylistStorageEntity::class,
        PlaylistSongsEntity::class,
        FavouritesEntity::class
    ],
    version = 14,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 10, to = 11),
        AutoMigration(from = 11, to = 12),
        AutoMigration(from = 12, to = 13),
        AutoMigration(from = 13, to = 14, spec = AutoMigration1314::class)
    ]
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun allPlaylistsDao(): AllPlaylistsDao
    abstract fun favouritesDao(): FavouritesDao
}
