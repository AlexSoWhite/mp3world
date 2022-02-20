package com.nafanya.mp3world.viewmodel.listViewModels

import com.nafanya.mp3world.model.wrappers.Playlist

object SourceProvider {

    var initializingPlaylist: Playlist? = null
    var query: String? = null

    fun newInstanceWithPlaylist(
        playlist: Playlist
    ) {
        this.initializingPlaylist = playlist
        this.query = null
    }
    fun newInstanceWithQuery(
        query: String
    ) {
        this.query = query
        this.initializingPlaylist = null
    }
}