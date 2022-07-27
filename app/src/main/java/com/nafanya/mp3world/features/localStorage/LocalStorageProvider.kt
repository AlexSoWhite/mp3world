package com.nafanya.mp3world.features.localStorage

import com.nafanya.mp3world.features.favorites.FavouriteListEntity
import com.nafanya.mp3world.features.playlists.playlist.toStorageEntity
import com.nafanya.mp3world.features.playlists.playlistsList.PlaylistListManager
import com.nafanya.player.Playlist
import javax.inject.Inject

// TODO refactor it
// TODO update comments
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
    suspend fun addPlaylist(playlist: Playlist) {
        dbHolder.db.playlistDao().insert(playlist.toStorageEntity())
    }

    /**
     * updating playlist from the local storage, creates a GlobalScope.launch
     */
    suspend fun updatePlaylist(playlist: Playlist) {
        val index = PlaylistListManager.playlists.value!!.indexOf(playlist)
        if (index != -1) {
            dbHolder.db.playlistDao().update(playlist.toStorageEntity())
        }
    }

    /**
     * deleting playlist from a local storage, creates a GlobalScope.launch
     */
    suspend fun deletePlaylist(playlist: Playlist) {
        dbHolder.db.playlistDao().delete(playlist.toStorageEntity())
    }

    // favourite list section
    suspend fun addFavourite(song: com.nafanya.player.Song) {
        dbHolder.db.favouriteListDao().insert(
            FavouriteListEntity(song.id)
        )
    }

    suspend fun deleteFavourite(song: com.nafanya.player.Song) {
        dbHolder.db.favouriteListDao().delete(
            FavouriteListEntity(song.id)
        )
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
    suspend fun populateLists() {
        dbHolder.populateLists()
    }
}
