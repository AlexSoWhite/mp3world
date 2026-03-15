package com.nafanya.player

interface Playlist {
    val songList: List<Song>
    fun areSongListsTheSame(other: Playlist): Boolean
}
