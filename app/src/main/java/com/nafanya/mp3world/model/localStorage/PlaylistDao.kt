package com.nafanya.mp3world.model.localStorage

import androidx.room.*
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
