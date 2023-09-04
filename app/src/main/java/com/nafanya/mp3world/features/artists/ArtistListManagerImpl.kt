package com.nafanya.mp3world.features.artists

import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

/**
 * Object that holds artists data. Populated by [MediaStoreInteractor].
 */
@Singleton
class ArtistListManagerImpl @Inject constructor(
    mediaStoreInteractor: MediaStoreInteractor
) : ArtistListManager {

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
    override val artists: Flow<List<Artist>>
        get() = mArtists

    override fun getPlaylistByContainerId(id: Long): Flow<PlaylistWrapper> {
        return artists.mapNotNull {
            it.firstOrNull { artist ->
                artist.id == id
            }?.playlist
        }
    }
}
