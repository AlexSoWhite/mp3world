package com.nafanya.mp3world.features.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.coroutines.collectLatestInScope
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.wrappers.LocalSong
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.features.favorites.model.FavouritesEntity
import com.nafanya.mp3world.features.localStorage.api.FavouritesInteractor
import com.nafanya.mp3world.features.localStorage.LocalStorageInteractor
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Object that holds favourites data. Managed by [LocalStorageInteractor] and [MediaStoreInteractor].
 */
@Singleton
class FavouritesManager @Inject constructor(
    private val favouriteListInteractor: FavouritesInteractor,
    mediaStoreInteractor: MediaStoreInteractor,
    ioCoroutineProvider: IOCoroutineProvider
) : ListManager() {

    private val mFavourites = MutableLiveData<PlaylistWrapper>()
    val favorites: LiveData<PlaylistWrapper>
        get() = mFavourites

    fun isSongInFavourites(song: LocalSong) = favorites
        .map { favourites ->
            favourites.songList.contains(song)
        }

    init {
        mediaStoreInteractor.allSongs.collectLatestInScope(
            ioCoroutineProvider.ioScope
        ) { songList ->
            favouriteListInteractor.getFavouritesUris().collectLatestInScope(
                ioCoroutineProvider.ioScope
            ) { uris ->
                val songs = songList
                    .sortedByDescending { song -> song.date }
                    .filter { uris.contains(it.uri) }
                // TODO: string resource
                val temp = PlaylistWrapper(
                    songList = songs,
                    name = "Избранное"
                )
                mFavourites.postValue(temp)
            }
        }
    }

    override fun getPlaylistByContainerId(id: Long): LiveData<PlaylistWrapper?> {
        return favorites.map { it }
    }

    suspend fun add(song: LocalSong) {
        favouriteListInteractor.addFavourite(FavouritesEntity(song.uri.toString()))
    }

    suspend fun delete(song: LocalSong) {
        favouriteListInteractor.deleteFavourite(FavouritesEntity(song.uri.toString()))
    }
}
