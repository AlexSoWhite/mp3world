package com.nafanya.mp3world.model.localStorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nafanya.mp3world.model.wrappers.PlaylistStorageEntity

@Dao
interface StoredPlaylistDao {
    @Query("SELECT * FROM playlistStorageEntity")
    fun getAll(): MutableList<PlaylistStorageEntity>

    @Update
    fun update(playlist: PlaylistStorageEntity)

    @Insert
    fun insert(vararg playlist: PlaylistStorageEntity)

    @Delete
    fun delete(playlist: PlaylistStorageEntity)
}
