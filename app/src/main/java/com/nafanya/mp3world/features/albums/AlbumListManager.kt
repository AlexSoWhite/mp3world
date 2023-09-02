package com.nafanya.mp3world.features.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Object that holds albums data. Populated by [MediaStoreInteractor]. Updates when [MediaStoreInteractor] is updated.
 */
@Singleton
class AlbumListManager @Inject constructor(
    mediaStoreInteractor: MediaStoreInteractor
) : ListManager() {

    private val mAlbums = mediaStoreInteractor
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
    val albums: Flow<List<Album>>
        get() = mAlbums

    override fun getPlaylistByContainerId(id: Long): LiveData<PlaylistWrapper?> {
        return albums.map {
            it.firstOrNull { album ->
                album.id == id
            }?.playlist
        }.asLiveData()
    }
}
