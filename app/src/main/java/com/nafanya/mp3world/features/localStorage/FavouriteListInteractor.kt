package com.nafanya.mp3world.features.localStorage

import android.net.Uri
import com.nafanya.mp3world.features.favorites.FavouriteListEntity
import kotlinx.coroutines.flow.Flow

interface FavouriteListInteractor {

    fun getFavouritesUris(): Flow<List<Uri>>

    suspend fun addFavourite(entity: FavouriteListEntity)

    suspend fun deleteFavourite(entity: FavouriteListEntity)
}
