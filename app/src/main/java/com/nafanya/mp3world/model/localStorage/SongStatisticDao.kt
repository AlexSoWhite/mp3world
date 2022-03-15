package com.nafanya.mp3world.model.localStorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nafanya.mp3world.model.wrappers.SongStatisticEntity

@Dao
interface SongStatisticDao {
    @Query("SELECT * FROM SongStatisticEntity")
    fun getAll(): MutableList<SongStatisticEntity>

    @Insert
    fun insert(value: SongStatisticEntity)

    @Update
    fun update(value: SongStatisticEntity)

    @Delete
    fun delete(value: SongStatisticEntity)
}