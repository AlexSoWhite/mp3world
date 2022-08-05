package com.nafanya.mp3world.features.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.mp3world.features.localStorage.FavouriteListDao
import com.nafanya.mp3world.features.localStorage.LocalStorageProvider
import com.nafanya.player.Playlist
import com.nafanya.player.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Object that holds favourites data. Managed by DataBaseHolder and LocalStorageProvider.
 */
@Singleton
class FavouriteListManager @Inject constructor(
    private val localStorageProvider: LocalStorageProvider
) : ListManager {

    private val mFavourites = MutableLiveData<Playlist>()
    val favorites: LiveData<Playlist>
        get() = mFavourites

    fun add(song: Song) {
        val temp = mFavourites.value?.copy()
        temp?.songList?.add(song)
        mFavourites.postValue(temp)
    }

    fun delete(song: Song) {
        val temp = mFavourites.value?.copy()
        temp?.songList?.remove(song)
        mFavourites.postValue(temp)
    }

    override suspend fun populate(mediaStoreReader: MediaStoreReader) {
        withContext(Dispatchers.IO) {
            val ids = localStorageProvider.dbHolder.db.favouriteListDao().getAll()
            val temp = Playlist(
                songList = mutableListOf(),
                name = "Избранное"
            )
            temp.songList.addAll(mediaStoreReader.allSongs.filter { ids.contains(it.id) })
            mFavourites.postValue(temp)
        }
    }
}
