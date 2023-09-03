package com.nafanya.mp3world.features.localStorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nafanya.mp3world.features.allPlaylists.model.PlaylistSongsEntity
import com.nafanya.mp3world.features.allPlaylists.model.PlaylistStorageEntity
import com.nafanya.mp3world.features.allPlaylists.model.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AllPlaylistsDao {
    @Transaction
    @Query("SELECT * FROM playlistStorageEntity")
    abstract fun getAll(): Flow<List<PlaylistWithSongs>>

    @Update
    abstract suspend fun update(playlist: PlaylistStorageEntity)

    @Insert
    abstract suspend fun insert(playlist: PlaylistStorageEntity)

    @Insert
    abstract suspend fun insertSongs(songs: List<PlaylistSongsEntity>)

    @Transaction
    open suspend fun update(
        oldPlaylist: PlaylistStorageEntity,
        newPlaylist: Pair<PlaylistStorageEntity, List<PlaylistSongsEntity>>
    ) {
        delete(oldPlaylist)
        insert(newPlaylist.first)
        insertSongs(newPlaylist.second)
    }

    @Delete
    abstract suspend fun delete(playlist: PlaylistStorageEntity)
}
