package com.nafanya.mp3world.features.favourites

import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.coroutines.collectLatestInScope
import com.nafanya.mp3world.core.coroutines.emitInScope
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.features.favourites.model.FavouritesEntity
import com.nafanya.mp3world.features.localStorage.LocalStorageInteractor
import com.nafanya.mp3world.features.localStorage.api.FavouritesInteractor
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

/**
 * Object that holds favourites data. Managed by [LocalStorageInteractor] and [MediaStoreInteractor].
 */
@Singleton
class FavouritesManager @Inject constructor(
    private val favouriteListInteractor: FavouritesInteractor,
    private val ioCoroutineProvider: IOCoroutineProvider,
    mediaStoreInteractor: MediaStoreInteractor
) : ListManager {

    private val mFavourites = MutableSharedFlow<PlaylistWrapper>(replay = 1)
    val favorites: Flow<PlaylistWrapper>
        get() = mFavourites

    fun isSongInFavourites(song: LocalSong) = mFavourites
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
                mFavourites.emitInScope(ioCoroutineProvider.ioScope, temp)
            }
        }
    }

    override fun getPlaylistByContainerId(id: Long) = favorites

    suspend fun add(song: LocalSong) {
        favouriteListInteractor.addFavourite(FavouritesEntity(song.uri.toString()))
    }

    suspend fun delete(song: LocalSong) {
        favouriteListInteractor.deleteFavourite(FavouritesEntity(song.uri.toString()))
    }
}
