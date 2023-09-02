package com.nafanya.mp3world.features.allSongs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.core.wrappers.LocalSong
import com.nafanya.mp3world.features.mediaStore.MediaStoreInteractor
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Class that holds all songs data
 */
@Singleton
class SongListManager @Inject constructor(
    mediaStoreInteractor: MediaStoreInteractor,
    ioCoroutineProvider: IOCoroutineProvider
) : ListManager() {

    private val mSongList = MutableLiveData<List<LocalSong>>()
    val songList: LiveData<List<LocalSong>>
        get() = mSongList

    init {
        ioCoroutineProvider.ioScope.launch {
            mediaStoreInteractor.allSongs.collectLatest {
                it.let { list ->
                    mSongList.postValue(list)
                }
            }
        }
    }

    override fun getPlaylistByContainerId(id: Long): LiveData<PlaylistWrapper?> {
        return songList.map {
            PlaylistWrapper(
                songList = it,
                name = "Мои песни"
            )
        }
    }
}

fun List<SongWrapper>.asAllSongsPlaylist(): PlaylistWrapper {
    return PlaylistWrapper(
        name = "Мои песни",
        songList = this
    )
}
