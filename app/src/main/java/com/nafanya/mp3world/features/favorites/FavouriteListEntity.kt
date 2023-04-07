package com.nafanya.mp3world.features.favorites

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Class that holds favourite song id. Can be added to local Room database.
 */
@Entity
data class FavouriteListEntity(
    @PrimaryKey var uri: String
)
