package com.nafanya.mp3world.model.listManagers

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.wrappers.Artist
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song

/**
 * Object that holds artists data. Populated by MediaStoreReader.
 */
object ArtistListManager {

    val artists: MutableLiveData<MutableList<Artist>> by lazy {
        MutableLiveData<MutableList<Artist>>(mutableListOf())
    }
    private var suspendedList = mutableListOf<Artist>()

    fun add(element: Artist, song: Song) {
        if (suspendedList.indexOf(element) != -1) {
            suspendedList.elementAt(suspendedList.indexOf(element))
                .playlist?.songList?.add(song)
        } else {
            element.playlist = Playlist(
                arrayListOf(song),
                id = 0,
                name = element.name!!
            )
            suspendedList.add(element)
        }
    }

    fun resetData() {
        artists.postValue(suspendedList)
        suspendedList = mutableListOf()
    }
}
