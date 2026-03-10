package com.nafanya.mp3world.data.local_storage.api

import android.net.Uri
import com.nafanya.mp3world.data.favourites.FavouritesEntity
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {

    fun observeFavouritesUris(): Flow<List<Uri>>

    suspend fun addFavourite(entity: FavouritesEntity)

    suspend fun deleteFavourite(entity: FavouritesEntity)
}
