package com.nafanya.mp3world.model

object ArtistListManager {

    var artists: ArrayList<Artist> = ArrayList()

    fun add(element: Artist, song: Song) {
        if (artists.indexOf(element) != -1) {
            this.artists[artists.indexOf(element)]
                .playlist?.songList?.add(song)
        } else {
            element.playlist = Playlist(arrayListOf(song))
            artists.add(element)
        }
    }
}
