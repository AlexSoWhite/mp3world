package com.nafanya.mp3world.features.favourites.domain

import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.coroutines.collectLatestInScope
import com.nafanya.mp3world.core.coroutines.emitInScope
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.data.favourites.FavouritesEntity
import com.nafanya.mp3world.data.local_storage.LocalStorageRepository
import com.nafanya.mp3world.data.local_storage.api.FavouritesRepository
import com.nafanya.mp3world.data.mediaStore.MediaStoreInteractor
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

/**
 * Object that holds favourites data. Managed by [LocalStorageRepository] and [MediaStoreInteractor].
 */
@Singleton
class FavouritesManagerImpl @Inject constructor(
    private val favouriteListInteractor: FavouritesRepository,
    private val ioCoroutineProvider: IOCoroutineProvider,
    mediaStoreInteractor: MediaStoreInteractor
) : FavouritesManager {

    private val _favorites = MutableSharedFlow<PlaylistWrapper>(replay = 1)
    override val favorites: Flow<PlaylistWrapper>
        get() = _favorites

    override fun observeIsSongInFavorites(song: LocalSong) = _favorites
        .map { favourites ->
            favourites.songList.contains(song)
        }

    init {
        // todo: this looks weird, should be revisited
        mediaStoreInteractor.allSongs.collectLatestInScope(
            ioCoroutineProvider.ioScope
        ) { songList ->
            favouriteListInteractor.observeFavouritesUris().collectLatestInScope(
                ioCoroutineProvider.ioScope
            ) { uris ->
                val songs = songList
                    .filter { uris.contains(it.uri) }
                // TODO: string resource
                val temp = PlaylistWrapper(
                    songList = songs,
                    name = "Избранное"
                )
                _favorites.emitInScope(ioCoroutineProvider.ioScope, temp)
            }
        }
    }

    override fun getPlaylistByContainerId(id: Long) = favorites

    override suspend fun add(song: LocalSong) {
        favouriteListInteractor.addFavourite(FavouritesEntity(song.uri.toString()))
    }

    override suspend fun delete(song: LocalSong) {
        favouriteListInteractor.deleteFavourite(FavouritesEntity(song.uri.toString()))
    }
}
