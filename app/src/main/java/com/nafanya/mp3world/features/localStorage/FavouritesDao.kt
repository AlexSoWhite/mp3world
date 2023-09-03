package com.nafanya.mp3world.features.localStorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nafanya.mp3world.features.favorites.model.FavouritesEntity
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
