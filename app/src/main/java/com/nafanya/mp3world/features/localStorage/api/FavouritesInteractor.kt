package com.nafanya.mp3world.features.localStorage.api

import android.net.Uri
import com.nafanya.mp3world.features.favourites.model.FavouritesEntity
import kotlinx.coroutines.flow.Flow

interface FavouritesInteractor {

    fun getFavouritesUris(): Flow<List<Uri>>

    suspend fun addFavourite(entity: FavouritesEntity)

    suspend fun deleteFavourite(entity: FavouritesEntity)
}
