package com.nafanya.mp3world.data.local_storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nafanya.mp3world.data.favourites.FavouritesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDao {
    @Query("SELECT * FROM favouritesEntity")
    fun getAll(): Flow<MutableList<String>>

    @Insert
    suspend fun insert(value: FavouritesEntity)

    @Delete
    suspend fun delete(value: FavouritesEntity)
}
