package com.nafanya.mp3world.features.allSongs

import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.coroutines.collectLatestInScope
import com.nafanya.mp3world.core.coroutines.emitInScope
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

/**
 * Class that holds all songs data
 */
@Singleton
class SongListManager @Inject constructor(
    mediaStoreInteractor: MediaStoreInteractor,
    private val ioCoroutineProvider: IOCoroutineProvider
) : ListManager {

    private val mSongList = MutableSharedFlow<List<LocalSong>>(replay = 1)
    val songList: Flow<List<LocalSong>>
        get() = mSongList

    init {
        mediaStoreInteractor.allSongs.collectLatestInScope(ioCoroutineProvider.ioScope) {
            mSongList.emitInScope(ioCoroutineProvider.ioScope, it)
        }
    }

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

// TODO: string res
fun List<SongWrapper>.asAllSongsPlaylist(): PlaylistWrapper {
    return PlaylistWrapper(
        name = "Мои песни",
        songList = this
    )
}
