package com.nafanya.mp3world.model.listManagers

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.wrappers.Song

object SongListManager {

    val songList: MutableLiveData<MutableList<Song>> by lazy {
        MutableLiveData<MutableList<Song>>(mutableListOf())
    }

    fun add(song: Song) {
        songList.value?.add(song)
    }
}
