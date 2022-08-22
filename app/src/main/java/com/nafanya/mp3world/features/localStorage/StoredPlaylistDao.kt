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

@Dao
interface StoredPlaylistDao {
    @Transaction
    @Query("SELECT * FROM playlistStorageEntity")
    suspend fun getAll(): List<PlaylistWithSongs>

    @Update
    suspend fun update(playlist: PlaylistStorageEntity)

    @Insert
    suspend fun insert(playlist: PlaylistStorageEntity)

    @Insert
    suspend fun insertSongs(songs: List<PlaylistSongsEntity>)

    @Delete
    suspend fun delete(playlist: PlaylistStorageEntity)
}
