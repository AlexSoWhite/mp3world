package com.nafanya.mp3world.domain.artists

import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.data.media_store.MediaStoreInteractor
import javax.inject.Inject
import kotlin.collections.forEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

/**
 * Object that holds artists data. Populated by [MediaStoreInteractor].
 */
class ArtistPlaylistProviderImpl @Inject constructor(
    mediaStoreInteractor: MediaStoreInteractor
) : ArtistPlaylistProvider {

    // todo: maybe can be simplified
    private val mArtists = mediaStoreInteractor
        .allSongs
        .map { list ->
            val artistsIdMap = mutableMapOf<String, Long>()
            val artistsSongsMap = mutableMapOf<String, List<SongWrapper>?>()
            list.forEach { song ->
                val artists = song.artists
                artists.forEach { artist ->
                    if (artistsIdMap.contains(artist.name)) {
                        artistsSongsMap[artist.name] = mutableListOf(song).plus(artistsSongsMap[artist.name]!!)
                    } else {
                        artistsIdMap[artist.name] = artist.id
                        artistsSongsMap[artist.name] = mutableListOf(song)
                    }
                }
            }

            artistsIdMap.map { (name, id) ->
                val songs = artistsSongsMap[name] ?: emptyList()
                Artist(
                    id = id,
                    name = name,
                    imageSource = songs.getOrNull(0),
                    playlist = PlaylistWrapper(
                        songList = songs,
                        name = name
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
