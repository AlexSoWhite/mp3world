package com.nafanya.mp3world.core.source

import com.nafanya.mp3world.features.playlists.playlist.model.Playlist

// TODO consider refactoring
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
