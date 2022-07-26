package com.nafanya.mp3world.features.artists

import androidx.lifecycle.MutableLiveData
import com.nafanya.player.Playlist
import com.nafanya.player.Song

// TODO consider refactoring
/**
 * Object that holds artists data. Populated by MediaStoreReader.
 */
object ArtistListManager {

    val artists: MutableLiveData<MutableList<Artist>> by lazy {
        MutableLiveData<MutableList<Artist>>(mutableListOf())
    }
    private var suspendedList = mutableListOf<Artist>()

    fun add(element: Artist, song: Song) {
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
        artists.postValue(suspendedList)
        suspendedList = mutableListOf()
    }
}
