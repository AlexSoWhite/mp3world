package com.nafanya.mp3world.features.allSongs

import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Class that holds all songs data
 */
@Singleton
class SongListManagerImpl @Inject constructor(
    mediaStoreInteractor: MediaStoreInteractor
) : SongListManager {

    private val mSongList = mediaStoreInteractor.allSongs
    override val songList: Flow<List<LocalSong>>
        get() = mSongList

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
