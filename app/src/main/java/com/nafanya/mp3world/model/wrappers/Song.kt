package com.nafanya.mp3world.model.wrappers

data class Song(
    val id: Long = 0,
    val title: String? = null,
    val artist: String? = null,
    val date: String? = null,
    val url: String? = null
)
