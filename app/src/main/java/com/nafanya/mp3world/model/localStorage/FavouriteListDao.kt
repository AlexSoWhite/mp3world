package com.nafanya.mp3world.model.localStorage

import androidx.room.*
import com.nafanya.mp3world.model.wrappers.FavouriteListEntity

@Dao
interface FavouriteListDao {
    @Query("SELECT * FROM favouriteListEntity")
    fun getAll(): MutableList<Long>

    @Insert
    fun insert(value: FavouriteListEntity)

    @Delete
    fun delete(value: FavouriteListEntity)
}
