package com.nafanya.mp3world.domain.all_songs

import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.data.media_store.MediaStoreInteractor
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Class that holds all songs data
 */
class SongPlaylistProviderImpl @Inject constructor(
    mediaStoreInteractor: MediaStoreInteractor
) : SongPlaylistProvider {

    private val _songList = mediaStoreInteractor.allSongs
    override val songList: Flow<List<LocalSong>>
        get() = _songList

    // TODO: string res
    override fun getPlaylistByContainerId(id: Long): Flow<PlaylistWrapper> {
        return songList.map {
            PlaylistWrapper(
                songList = it,
                name = "Мои песни"
            )
        }
    }
}
