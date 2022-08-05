package com.nafanya.mp3world.features.allSongs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.listManagers.ListManager
import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import com.nafanya.player.Song
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Class that holds all songs data
 */
@Singleton
class SongListManager @Inject constructor() : ListManager {

    private val mSongList: MutableLiveData<List<Song>> = MutableLiveData(listOf())
    val songList: LiveData<List<Song>>
        get() = mSongList

    private var isInitialized = false

    override suspend fun populate(
        mediaStoreReader: MediaStoreReader
    ) {
        if (isInitialized) {
            mSongList.postValue(mediaStoreReader.allSongs)
        } else {
            // postValue doesn't trigger anything if there are no subscribers
            // so initialization Service normally handle only setValue
            withContext(Dispatchers.Main) {
                mSongList.value = mediaStoreReader.allSongs
            }
        }
    }

    fun searchForSongs(query: String): List<Song> {
        val result = mutableListOf<Song>()
        result.addAll(
            songList.value!!.filter {
                it.title.lowercase().contains(query.lowercase()) ||
                    it.artist.lowercase().contains(query.lowercase()) ||
                    it.album.lowercase().contains(query.lowercase())
            }
        )
        return result
    }
}
