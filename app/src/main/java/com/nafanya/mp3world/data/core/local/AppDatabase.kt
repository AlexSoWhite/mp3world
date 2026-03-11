package com.nafanya.mp3world.data.core.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nafanya.mp3world.data.user_playlists.UserPlaylistsDao
import com.nafanya.mp3world.data.local_storage.FavouritesDao
import com.nafanya.mp3world.data.user_playlists.PlaylistSongsEntity
import com.nafanya.mp3world.data.user_playlists.PlaylistStorageEntity
import com.nafanya.mp3world.data.favourites.FavouritesEntity

@Database(
    entities = [
        PlaylistStorageEntity::class,
        PlaylistSongsEntity::class,
        FavouritesEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userPlaylistsDao(): UserPlaylistsDao
    abstract fun favouritesDao(): FavouritesDao
}
