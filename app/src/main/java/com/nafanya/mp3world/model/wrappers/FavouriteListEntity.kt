package com.nafanya.mp3world.model.wrappers

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Class that holds favourite song id. Can be added to local Room database.
 */
@Entity
data class FavouriteListEntity(
    @PrimaryKey var id: Long
)
