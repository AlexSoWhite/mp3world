package com.nafanya.mp3world.model.localStorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nafanya.mp3world.model.wrappers.Song

@Dao
interface SongDao {

    @Query("SELECT * FROM song")
    fun getAll(): MutableList<Song>

    @Query("SELECT * FROM song WHERE id = (:songId)")
    fun loadById(songId: Int): Song

    @Update
    fun update(song: Song)

    @Insert
    fun insert(vararg song: Song)

    @Delete
    fun delete(song: Song)
}
