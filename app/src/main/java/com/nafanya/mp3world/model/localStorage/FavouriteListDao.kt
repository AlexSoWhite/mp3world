package com.nafanya.mp3world.model.localStorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nafanya.mp3world.model.wrappers.FavouriteListEntity

@Dao
interface FavouriteListDao {
    @Query("SELECT * FROM favouriteListEntity")
    suspend fun getAll(): MutableList<Long>

    @Insert
    suspend fun insert(value: FavouriteListEntity)

    @Delete
    suspend fun delete(value: FavouriteListEntity)
}
