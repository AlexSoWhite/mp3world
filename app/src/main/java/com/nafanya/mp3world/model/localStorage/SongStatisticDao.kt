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
    suspend fun getAll(): MutableList<SongStatisticEntity>

    @Insert
    suspend fun insert(value: SongStatisticEntity)

    @Update
    suspend fun update(value: SongStatisticEntity)

    @Delete
    suspend fun delete(value: SongStatisticEntity)
}
