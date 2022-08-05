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
    val dbHolder: DatabaseHolder
) {

    // playlist section
    /**
     * adding playlist to the local storage
     */
    suspend fun addPlaylist(playlist: Playlist) {
        dbHolder.db.playlistDao().insert(playlist.toStorageEntity())
    }

    /**
     * updating playlist from the local storage
     */
    suspend fun updatePlaylist(playlist: Playlist, playlistListManager: PlaylistListManager) {
        val index = playlistListManager.playlists.value!!.indexOf(playlist)
        if (index != -1) {
            dbHolder.db.playlistDao().update(playlist.toStorageEntity())
        }
    }

    /**
     * deleting playlist from a local storage
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
}
