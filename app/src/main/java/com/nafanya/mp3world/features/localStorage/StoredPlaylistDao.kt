package com.nafanya.mp3world.features.localStorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nafanya.mp3world.features.playlists.playlist.model.PlaylistStorageEntity

@Dao
interface StoredPlaylistDao {
    @Query("SELECT * FROM playlistStorageEntity")
    suspend fun getAll(): MutableList<PlaylistStorageEntity>

    @Update
    suspend fun update(playlist: PlaylistStorageEntity)

    @Insert
    suspend fun insert(vararg playlist: PlaylistStorageEntity)

    @Delete
    suspend fun delete(playlist: PlaylistStorageEntity)
}
