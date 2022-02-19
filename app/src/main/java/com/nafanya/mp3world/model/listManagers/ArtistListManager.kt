package com.nafanya.mp3world.model.listManagers

import com.nafanya.mp3world.model.wrappers.Artist
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song

object ArtistListManager {

    var artists: ArrayList<Artist> = ArrayList()

    fun add(element: Artist, song: Song) {
        if (artists.indexOf(element) != -1) {
            artists[artists.indexOf(element)]
                .playlist?.songList?.add(song)
        } else {
            element.playlist = Playlist(
                arrayListOf(song),
                id = 0,
                name = element.name!!
            )
            artists.add(element)
        }
    }
}
