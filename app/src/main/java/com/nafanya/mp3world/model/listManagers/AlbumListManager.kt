package com.nafanya.mp3world.model.listManagers

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.wrappers.Album
import com.nafanya.mp3world.model.wrappers.Song

object AlbumListManager {

    val albums: MutableLiveData<MutableList<Album>> by lazy {
        MutableLiveData<MutableList<Album>>(mutableListOf())
    }

    fun add(element: Album, song: Song) {
        if (albums.value?.indexOf(element) != -1) {
            albums.value?.elementAt(albums.value?.indexOf(element)!!)!!.songList.add(song)
        } else {
            element.songList = mutableListOf(song)
            albums.value?.add(element)
        }
    }
}
