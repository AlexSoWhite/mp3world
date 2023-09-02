package com.nafanya.mp3world.features.favorites

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.localStorage.DatabaseHolder
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Object that holds favourites data. Managed by [DatabaseHolder] and [MediaStoreInteractor].
 */
@Singleton
class FavouriteListManager @Inject constructor(
    mediaStoreInteractor: MediaStoreInteractor,
    ioCoroutineProvider: IOCoroutineProvider,
    private val dbHolder: DatabaseHolder
) : ListManager() {

    private val mFavourites = MutableLiveData<PlaylistWrapper>()
    val favorites: LiveData<PlaylistWrapper>
        get() = mFavourites

    init {
        ioCoroutineProvider.ioScope.launch {
            mediaStoreInteractor.allSongs.collectLatest { rawList ->
                rawList?.let { songList ->
                    val uris = dbHolder.db.favouriteListDao().getAll().map { Uri.parse(it) }
                    val songs = songList
                        .sortedByDescending { song -> song.date }
                        .filter { uris.contains(it.uri) }
                    val temp = PlaylistWrapper(
                        songList = songs,
                        name = "Избранное"
                    )
                    mFavourites.postValue(temp)
                }
            }
        }
    }

    override fun getPlaylistByContainerId(id: Long): LiveData<PlaylistWrapper?> {
        return favorites.map { it }
    }

    suspend fun add(song: SongWrapper) {
        val temp = mutableListOf<SongWrapper>()
        favorites.value?.songList?.forEach {
            temp.add(it)
        }
        dbHolder.db.favouriteListDao().insert(FavouriteListEntity(song.uri.toString()))
        mFavourites.postValue(
            PlaylistWrapper(
                songList = listOf(song) + temp,
                name = "Избранное"
            )
        )
    }

    suspend fun delete(song: SongWrapper) {
        val temp: MutableList<SongWrapper> = favorites.value?.songList as MutableList<SongWrapper>
        temp.remove(song)
        dbHolder.db.favouriteListDao().delete(FavouriteListEntity(song.uri.toString()))
        mFavourites.postValue(
            PlaylistWrapper(
                songList = temp,
                id = -1,
                name = "Избранное"
            )
        )
    }
}
