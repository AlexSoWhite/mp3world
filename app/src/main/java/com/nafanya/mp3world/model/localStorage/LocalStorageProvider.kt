package com.nafanya.mp3world.model.localStorage

import com.nafanya.mp3world.model.dependencies.PlayerApplication
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.wrappers.FavouriteListEntity
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.model.wrappers.SongStatisticEntity
import kotlin.concurrent.thread

class LocalStorageProvider {

    private var context = PlayerApplication.context()

    // playlist section
    /**
     * adding playlist to the local storage, creates a thread
     */
    fun addPlaylist(playlist: Playlist) {
        thread {
            val dbHolder = DatabaseHolder()
            dbHolder.db.playlistDao().insert(playlist.toStorageEntity())
            dbHolder.closeDataBase()
        }
    }

    /**
     * updating playlist from the local storage, creates a thread
     */
    fun updatePlaylist(playlist: Playlist) {
        thread {
            val dbHolder = DatabaseHolder()
            val index = PlaylistListManager.playlists.value!!.indexOf(playlist)
            if (index != -1) {
                dbHolder.db.playlistDao().update(playlist.toStorageEntity())
            }
            dbHolder.closeDataBase()
        }
    }

    /**
     * deleting playlist from a local storage, creates a thread
     */
    fun deletePlaylist(playlist: Playlist) {
        thread {
            val dbHolder = DatabaseHolder()
            dbHolder.db.playlistDao().delete(playlist.toStorageEntity())
            dbHolder.closeDataBase()
        }
    }

    // favourite list section
    fun addFavourite(song: Song) {
        thread {
            val dbHolder = DatabaseHolder()
            dbHolder.db.favouriteListDao().insert(
                FavouriteListEntity(song.id)
            )
            dbHolder.closeDataBase()
        }
    }

    fun deleteFavourite(song: Song) {
        thread {
            val dbHolder = DatabaseHolder()
            dbHolder.db.favouriteListDao().delete(
                FavouriteListEntity(song.id)
            )
            dbHolder.closeDataBase()
        }
    }

    // statistic section
    fun addStatisticEntity(value: SongStatisticEntity) {
//        thread {
//            val dbHolder = DatabaseHolder(context)
//            dbHolder.db.songStatisticDao().insert(value)
//            dbHolder.closeDataBase()
//        }
    }

    fun updateStatisticEntity(value: SongStatisticEntity) {
//        thread {
//            val dbHolder = DatabaseHolder(context)
//            dbHolder.db.songStatisticDao().update(value)
//            dbHolder.closeDataBase()
//        }
    }

    // all list section
    /**
     * populating lists
     */
    fun populateLists() {
        thread {
            val dbHolder = DatabaseHolder()
            dbHolder.populateLists()
            dbHolder.closeDataBase()
        }
    }
}
