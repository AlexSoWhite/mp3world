package com.nafanya.mp3world.model.listManagers

import com.nafanya.mp3world.model.localStorage.AppDatabase
import com.nafanya.mp3world.model.localStorage.DatabaseInitializer
import com.nafanya.mp3world.model.localStorage.PlaylistDao
import com.nafanya.mp3world.model.wrappers.Playlist

object PlaylistListManager {

    private lateinit var db: AppDatabase
    private lateinit var playlistDao: PlaylistDao
    lateinit var playlists: MutableList<Playlist>

    fun initialize() {
        db = DatabaseInitializer.db
        playlistDao = db.playlistDao()
        playlists = playlistDao.getAll()
    }

    fun addPlaylist(playlist: Playlist) {
        // playlistDao.insert(playlist)
        playlists.add(playlist)
    }

    fun getPlaylistBuName(name: String): Playlist? {
        playlists.forEach {
            if (it.name == name) return it
        }
        return null
    }
}
