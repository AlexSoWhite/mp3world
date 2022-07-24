package com.nafanya.mp3world.features.favorites

import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.domain.Song
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.mp3world.features.localStorage.FavouriteListDao
import com.nafanya.mp3world.features.playlists.playlist.Playlist
import kotlinx.coroutines.runBlocking

/**
 * Object that holds favourites data. Managed by DataBaseHolder and LocalStorageProvider.
 */
object FavouriteListManager {

    val favorites: MutableLiveData<Playlist> by lazy {
        MutableLiveData<Playlist>()
    }

    fun add(song: Song) {
        val temp = favorites.value
        temp?.songList?.add(song)
        favorites.postValue(temp)
    }

    fun delete(song: Song) {
        val temp = favorites.value
        temp?.songList?.remove(song)
        favorites.postValue(temp)
    }

    fun initialize(dao: FavouriteListDao) = runBlocking {
        val ids = dao.getAll()
        val temp = Playlist(
            songList = mutableListOf(),
            name = "Избранное"
        )
        ids.forEach { id ->
            SongListManager.songList.value?.forEach { song ->
                if (id == song.id) {
                    temp.songList.add(song)
                }
            }
        }
        favorites.postValue(temp)
    }
}
