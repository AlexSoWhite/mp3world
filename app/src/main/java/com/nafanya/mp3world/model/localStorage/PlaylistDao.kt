package com.nafanya.mp3world.model.localStorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nafanya.mp3world.model.wrappers.Playlist

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlist")
    fun getAll(): MutableList<Playlist>

    @Query("SELECT * FROM playlist WHERE id = (:playlistId)")
    fun loadById(playlistId: Int): Playlist

    @Update
    fun update(playlist: Playlist)

    @Insert
    fun insert(vararg playlist: Playlist)

    @Delete
    fun delete(playlist: Playlist)
}
