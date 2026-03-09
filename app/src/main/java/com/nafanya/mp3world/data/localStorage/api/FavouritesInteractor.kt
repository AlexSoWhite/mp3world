package com.nafanya.mp3world.data.localStorage.api

import android.net.Uri
import com.nafanya.mp3world.features.favourites.data.FavouritesEntity
import kotlinx.coroutines.flow.Flow

interface FavouritesInteractor {

    fun getFavouritesUris(): Flow<List<Uri>>

    suspend fun addFavourite(entity: FavouritesEntity)

    suspend fun deleteFavourite(entity: FavouritesEntity)
}
