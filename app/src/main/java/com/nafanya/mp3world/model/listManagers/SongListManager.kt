package com.nafanya.mp3world.model.listManagers

import com.nafanya.mp3world.model.wrappers.Song

object SongListManager {

    var songList: ArrayList<Song> = ArrayList()

    fun add(song: Song) {
        songList.add(song)
    }
}
