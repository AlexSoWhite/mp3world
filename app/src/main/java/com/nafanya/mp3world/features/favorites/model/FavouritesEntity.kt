package com.nafanya.mp3world.features.favorites.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Class that holds favourite song id. Can be added to local Room database.
 */
@Entity
data class FavouritesEntity(
    @PrimaryKey var uri: String
)
