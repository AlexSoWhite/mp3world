package com.nafanya.mp3world.model.listManagers

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.wrappers.Album
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song

/**
 * Object that holds albums data. Populated by MediaStoreReader.
 */
object AlbumListManager {

    val albums: MutableLiveData<MutableList<Album>> by lazy {
        MutableLiveData<MutableList<Album>>(mutableListOf())
    }
    private var suspendedList = mutableListOf<Album>()

    fun add(element: Album, song: Song) {
        val index = suspendedList.indexOf(element)
        if (index != -1) {
            suspendedList
                .elementAt(index)
                .playlist
                ?.songList
                ?.add(song)
        } else {
            element.playlist = Playlist(
                arrayListOf(song),
                name = element.name
            )
            suspendedList.add(element)
        }
    }

    fun resetData() {
        albums.postValue(suspendedList)
        suspendedList = mutableListOf()
    }
}
