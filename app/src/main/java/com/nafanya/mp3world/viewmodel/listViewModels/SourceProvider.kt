package com.nafanya.mp3world.viewmodel.listViewModels

import com.nafanya.mp3world.model.wrappers.Playlist

@Deprecated(message = "Use inject")
object SourceProvider {

    private var initializingPlaylist: Playlist? = null
    private var initializingQuery: String? = null

    fun newInstanceWithPlaylist(
        playlist: Playlist
    ) {
        this.initializingPlaylist = playlist
        this.initializingQuery = null
    }

    fun newInstanceWithQuery(
        query: String
    ) {
        this.initializingQuery = query
        this.initializingPlaylist = null
    }

    fun getPlaylist(): Playlist? {
        if (this.initializingPlaylist != null) {
            val playlist = this.initializingPlaylist
            this.initializingPlaylist = null
            return playlist
        }
        return null
    }

    fun getQuery(): String? {
        if (this.initializingQuery != null) {
            val query = this.initializingQuery
            this.initializingQuery = null
            return query
        }
        return null
    }
}
