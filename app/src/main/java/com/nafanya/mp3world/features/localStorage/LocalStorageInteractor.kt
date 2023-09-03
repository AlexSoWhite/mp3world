package com.nafanya.mp3world.features.localStorage

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.room.Room
import com.nafanya.mp3world.features.allPlaylists.model.PlaylistSongsEntity
import com.nafanya.mp3world.features.allPlaylists.model.PlaylistStorageEntity
import com.nafanya.mp3world.features.allPlaylists.model.PlaylistWithSongs
import com.nafanya.mp3world.features.favorites.FavouriteListEntity
import com.nafanya.mp3world.features.localStorage.migrations.Migration12
import com.nafanya.mp3world.features.localStorage.migrations.Migration23
import com.nafanya.mp3world.features.localStorage.migrations.Migration34
import com.nafanya.mp3world.features.localStorage.migrations.Migration45
import com.nafanya.mp3world.features.localStorage.migrations.Migration56
import com.nafanya.mp3world.features.localStorage.migrations.Migration67
import com.nafanya.mp3world.features.localStorage.migrations.Migration78
import com.nafanya.mp3world.features.localStorage.migrations.Migration89
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Suppress("MagicNumber")
/**
 * Class that provides access to local Room database. Populates playlists, favourites and statistics.
 * @property context holds application context.
 */
class LocalStorageInteractor(
    private val context: Context
) : FavouriteListInteractor, AllPlaylistsListInteractor {

    private val db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "appDb"
    ).addMigrations(
        Migration12,
        Migration23,
        Migration34,
        Migration45,
        Migration56,
        Migration67,
        Migration78,
        Migration89
    ).build()

    override fun getFavouritesUris(): Flow<List<Uri>> {
        return db.favouriteListDao().getAll().map { list -> list.map { it.toUri() } }
    }

    override suspend fun addFavourite(entity: FavouriteListEntity) {
        db.favouriteListDao().insert(entity)
    }

    override suspend fun deleteFavourite(entity: FavouriteListEntity) {
        db.favouriteListDao().delete(entity)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistWithSongs>> {
        return db.allPlaylistsListDao().getAll()
    }

    override suspend fun insert(entity: PlaylistStorageEntity) {
        db.allPlaylistsListDao().insert(entity)
    }

    override suspend fun insertSongs(songs: List<PlaylistSongsEntity>) {
        db.allPlaylistsListDao().insertSongs(songs)
    }

    override suspend fun delete(entity: PlaylistStorageEntity) {
        db.allPlaylistsListDao().delete(entity)
    }

    override suspend fun update(entity: PlaylistStorageEntity) {
        db.allPlaylistsListDao().update(entity)
    }

    override suspend fun update(
        oldEntity: PlaylistStorageEntity,
        newEntity: Pair<PlaylistStorageEntity, List<PlaylistSongsEntity>>
    ) {
        db.allPlaylistsListDao().update(oldEntity, newEntity)
    }

    fun closeDataBase() {
        db.close()
    }
}
