package com.nafanya.mp3world.model.wrappers

import android.graphics.Bitmap

/**
 * Class that wraps song.
 * @property id is taken from MediaStore.
 */
data class Song(
    var id: Long = 0,
    var title: String? = null,
    var artist: String? = null,
    var date: Long? = null,
    var url: String? = null,
    var duration: Long? = null,
    val art: Bitmap? = null,
    val artUrl: String? = null
)
