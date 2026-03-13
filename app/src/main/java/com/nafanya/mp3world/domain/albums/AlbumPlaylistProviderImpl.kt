package com.nafanya.mp3world.domain.albums

import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.data.media_store.MediaStoreInteractor
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

/**
 * Object that holds albums data. Populated by [MediaStoreInteractor]. Updates when [MediaStoreInteractor] is updated.
 */
class AlbumPlaylistProviderImpl @Inject constructor(
    mediaStoreInteractor: MediaStoreInteractor
) : AlbumPlaylistProvider {

    private val _albums = mediaStoreInteractor
        .allSongs
        .map { list ->
            list.groupBy {
                Pair(it.albumId, it.album)
            }.map {
                Album(
                    id = it.key.first,
                    name = it.key.second,
                    imageSource = it.value[0],
                    playlist = PlaylistWrapper(
                        it.value,
                        name = it.key.second
                    )
                )
            }
        }

    override val albums: Flow<List<Album>>
        get() = _albums

    override fun getPlaylistByContainerId(id: Long): Flow<PlaylistWrapper> {
        return albums.mapNotNull {
            it.firstOrNull { album ->
                album.id == id
            }?.playlist
        }
    }
}
