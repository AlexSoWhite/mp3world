package com.nafanya.mp3world.model.dependencies

import com.nafanya.mp3world.model.wrappers.Playlist

object SourceProvider {

    private var initializingPlaylist: Playlist? = null
    private var initializingQuery: String? = null

    fun putPlaylist(
        playlist: Playlist
    ) {
        initializingPlaylist = playlist
        initializingQuery = null
    }

    fun putQuery(
        query: String
    ) {
        initializingQuery = query
        initializingPlaylist = null
    }

    fun takePlaylist(): Playlist? {
        if (initializingPlaylist != null) {
            val playlist = initializingPlaylist
            initializingPlaylist = null
            return playlist
        }
        return null
    }

    fun takeQuery(): String? {
        if (initializingQuery != null) {
            val query = initializingQuery
            initializingQuery = null
            return query
        }
        return null
    }
}
