package com.nafanya.mp3world.model.listManagers

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.wrappers.Album
import com.nafanya.mp3world.model.wrappers.Song

object AlbumListManager {

    val albums: MutableLiveData<MutableList<Album>> by lazy {
        MutableLiveData<MutableList<Album>>(mutableListOf())
    }
    private var suspendedList = mutableListOf<Album>()

    fun add(element: Album, song: Song) {
        if (suspendedList.indexOf(element) != -1) {
            suspendedList.elementAt(suspendedList.indexOf(element)).songList.add(song)
        } else {
            element.songList = mutableListOf(song)
            suspendedList.add(element)
        }
    }

    fun resetData() {
        albums.postValue(suspendedList)
        suspendedList = mutableListOf()
    }
}
