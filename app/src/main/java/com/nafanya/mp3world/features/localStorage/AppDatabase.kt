package com.nafanya.mp3world.features.localStorage

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
    version = 10,
    exportSchema = true
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): StoredPlaylistDao
    abstract fun favouriteListDao(): FavouriteListDao
}
