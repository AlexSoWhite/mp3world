package com.nafanya.mp3world.model.wrappers

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteListEntity(
    @PrimaryKey var id: Long
)