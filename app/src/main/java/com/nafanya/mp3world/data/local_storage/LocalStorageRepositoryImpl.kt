package com.nafanya.mp3world.data.local_storage

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.room.Room
import com.nafanya.mp3world.core.coroutines.DispatchersProvider
import com.nafanya.mp3world.data.core.local.AppDatabase
import com.nafanya.mp3world.data.user_playlists.PlaylistSongsEntity
import com.nafanya.mp3world.data.user_playlists.PlaylistStorageEntity
import com.nafanya.mp3world.data.user_playlists.PlaylistWithSongs
import com.nafanya.mp3world.data.favourites.FavouritesEntity
import com.nafanya.mp3world.data.local_storage.api.UserPlaylistsRepository
import com.nafanya.mp3world.data.local_storage.api.FavouritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Suppress("MagicNumber")
/**
 * Class that provides access to local Room database. Populates playlists, favourites and statistics.
 * @property context holds application context.
 */
internal class LocalStorageRepositoryImpl(
    private val context: Context,
    private val dispatchersProvider: DispatchersProvider
) : LocalStorageRepository {

    private val db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "appDb"
    ).build()

    override fun observeFavouritesUris(): Flow<List<Uri>> {
        return db.favouritesDao().getAll().map { list -> list.map { it.toUri() } }
    }

    override suspend fun addFavourite(entity: FavouritesEntity) = withContext(dispatchersProvider.io) {
        db.favouritesDao().insert(entity)
    }

    override suspend fun deleteFavourite(entity: FavouritesEntity) = withContext(dispatchersProvider.io) {
        db.favouritesDao().delete(entity)
    }

    override fun observeUserPlaylists(): Flow<List<PlaylistWithSongs>> {
        return db.userPlaylistsDao().getAll()
    }

    override suspend fun insert(entity: PlaylistStorageEntity) = withContext(dispatchersProvider.io) {
        db.userPlaylistsDao().insert(entity)
    }

    override suspend fun insertSongs(songs: List<PlaylistSongsEntity>) = withContext(dispatchersProvider.io) {
        db.userPlaylistsDao().insertSongs(songs)
    }

    override suspend fun delete(entity: PlaylistStorageEntity) = withContext(dispatchersProvider.io) {
        db.userPlaylistsDao().delete(entity)
    }

    override suspend fun update(entity: PlaylistStorageEntity) = withContext(dispatchersProvider.io) {
        db.userPlaylistsDao().update(entity)
    }

    override suspend fun update(
        oldEntity: PlaylistStorageEntity,
        newEntity: Pair<PlaylistStorageEntity, List<PlaylistSongsEntity>>
    ) = withContext(dispatchersProvider.io) {
        db.userPlaylistsDao().update(oldEntity, newEntity)
    }

    fun closeDatabase() {
        db.close()
    }
}
