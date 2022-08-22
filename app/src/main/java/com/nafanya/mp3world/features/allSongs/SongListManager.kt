package com.nafanya.mp3world.features.allSongs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Class that holds all songs data
 */
@Singleton
class SongListManager @Inject constructor(
    mediaStoreReader: MediaStoreReader
) : ListManager() {

    private val mSongList = MutableLiveData<List<LocalSong>>()
    val songList: LiveData<List<LocalSong>>
        get() = mSongList

    private var isInitialized = false

    init {
        mediaStoreReader.allSongs.observeForever {
            listManagerScope.launch {
                if (isInitialized) {
                    mSongList.postValue(it)
                } else {
                    withContext(Dispatchers.Main) {
                        mSongList.value = it
                    }
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
