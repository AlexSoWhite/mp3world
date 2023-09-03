package com.nafanya.mp3world.features.artists

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
 * Object that holds artists data. Populated by [MediaStoreInteractor].
 */
@Singleton
class ArtistListManager @Inject constructor(
    mediaStoreInteractor: MediaStoreInteractor
) : ListManager() {

    private val mArtists = mediaStoreInteractor
        .allSongs
        .map { list ->
            list.groupBy {
                Pair(it.artistId, it.artist)
            }.map {
                Artist(
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
    val artists: Flow<List<Artist>>
        get() = mArtists

    override fun getPlaylistByContainerId(id: Long): LiveData<PlaylistWrapper?> {
        return artists.map {
            it.firstOrNull { artist ->
                artist.id == id
            }?.playlist
        }.asLiveData()
    }
}
