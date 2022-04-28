package com.nafanya.mp3world.model.localStorage

import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.wrappers.FavouriteListEntity
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Class that wraps DataBaseHolder work.
 */
class LocalStorageProvider @Inject constructor(
    var dbHolder: DatabaseHolder
) {

    // playlist section
    /**
     * adding playlist to the local storage, creates a GlobalScope.launch
     */
    fun addPlaylist(playlist: Playlist) = runBlocking {
        launch {
            dbHolder.db.playlistDao().insert(playlist.toStorageEntity())
            dbHolder.closeDataBase()
        }
    }

    /**
     * updating playlist from the local storage, creates a GlobalScope.launch
     */
    fun updatePlaylist(playlist: Playlist) = runBlocking {
        launch {
            val index = PlaylistListManager.playlists.value!!.indexOf(playlist)
            if (index != -1) {
                dbHolder.db.playlistDao().update(playlist.toStorageEntity())
            }
            dbHolder.closeDataBase()
        }
    }

    /**
     * deleting playlist from a local storage, creates a GlobalScope.launch
     */
    fun deletePlaylist(playlist: Playlist) = runBlocking {
        launch {
            dbHolder.db.playlistDao().delete(playlist.toStorageEntity())
            dbHolder.closeDataBase()
        }
    }

    // favourite list section
    fun addFavourite(song: Song) = runBlocking {
        launch {
            dbHolder.db.favouriteListDao().insert(
                FavouriteListEntity(song.id)
            )
            dbHolder.closeDataBase()
        }
    }

    fun deleteFavourite(song: Song) = runBlocking {
        launch {
            dbHolder.db.favouriteListDao().delete(
                FavouriteListEntity(song.id)
            )
            dbHolder.closeDataBase()
        }
    }

    // statistic section
//    fun addStatisticEntity(value: SongStatisticEntity) {
//        GlobalScope.launch {
//            val dbHolder = DatabaseHolder(context)
//            dbHolder.db.songStatisticDao().insert(value)
//            dbHolder.closeDataBase()
//        }
//    }

//    fun updateStatisticEntity(value: SongStatisticEntity) {
//        GlobalScope.launch {
//            val dbHolder = DatabaseHolder(context)
//            dbHolder.db.songStatisticDao().update(value)
//            dbHolder.closeDataBase()
//        }
//    }

    // all list section
    /**
     * populating lists
     */
    fun populateLists() = runBlocking {
        launch {
            dbHolder.populateLists()
            dbHolder.closeDataBase()
        }
    }
}
