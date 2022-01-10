package com.nafanya.mp3world.model

enum class Modes {
    CONSISTENT,
    LOOP
}

data class Playlist(
    var songList: ArrayList<Song>,
    var mode: Modes = Modes.CONSISTENT
)
