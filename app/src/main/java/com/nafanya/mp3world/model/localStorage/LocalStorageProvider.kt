package com.nafanya.mp3world.model.localStorage

import android.content.Context
import android.util.Log
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import kotlin.concurrent.thread

object LocalStorageProvider {

    // song section
    /**
     * adding song to the local storage, creates a thread
     */
    fun addSong(context: Context, song: Song) {
        thread {
            val dbHolder = DatabaseHolder(context)
            dbHolder.db.songsListDao().insert(song)
            dbHolder.closeDataBase()
        }
    }

    fun deleteSong(context: Context, song: Song) {
        thread {
            val dbHolder = DatabaseHolder(context)
            dbHolder.db.songsListDao().delete(song)
            dbHolder.closeDataBase()
        }
    }

    // playlist section
    /**
     * adding playlist to the local storage, creates a thread
     */
    fun addPlaylist(context: Context, playlist: Playlist) {
        thread {
            val dbHolder = DatabaseHolder(context)
            dbHolder.db.playlistDao().insert(playlist)
            dbHolder.closeDataBase()
        }
    }

    /**
     * updating playlist from the local storage, creates a thread
     */
    fun updatePlaylist(context: Context, playlist: Playlist) {
        thread {
            val dbHolder = DatabaseHolder(context)
            val index = PlaylistListManager.playlists.value!!.indexOf(playlist)
            if (index != -1) {
                dbHolder.db.playlistDao().update(playlist)
            }
            dbHolder.closeDataBase()
        }
    }

    /**
     * deleting playlist from a local storage, creates a thread
     */
    fun deletePlaylist(context: Context, playlist: Playlist) {
        thread {
            val dbHolder = DatabaseHolder(context)
            dbHolder.db.playlistDao().delete(playlist)
            dbHolder.closeDataBase()
        }
    }

    // all list section
    /**
     * populating lists
     */
    fun populateLists(context: Context) {
        thread {
            val dbHolder = DatabaseHolder(context)
            dbHolder.populateLists()
            dbHolder.closeDataBase()
        }
    }
}
