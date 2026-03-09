package com.nafanya.mp3world.features.favourites.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Class that holds favourite song id. Can be added to local Room database.
 *
 * todo: this should not be here
 */
@Entity
data class FavouritesEntity(
    @PrimaryKey var uri: String
)
