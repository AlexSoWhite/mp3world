package com.nafanya.mp3world.model.listManagers

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.model.wrappers.Artist
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song

object ArtistListManager {

    val artists: MutableLiveData<MutableList<Artist>> by lazy {
        MutableLiveData<MutableList<Artist>>(mutableListOf())
    }

    fun add(element: Artist, song: Song) {
        if (artists.value?.indexOf(element) != -1) {
            artists.value?.elementAt(artists.value?.indexOf(element)!!)!!
                .playlist?.songList?.add(song)
        } else {
            element.playlist = Playlist(
                arrayListOf(song),
                id = 0,
                name = element.name!!
            )
            artists.value?.add(element)
        }
    }
}
