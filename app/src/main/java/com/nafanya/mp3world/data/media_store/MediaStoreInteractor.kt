package com.nafanya.mp3world.data.media_store

import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import kotlinx.coroutines.flow.SharedFlow

interface MediaStoreInteractor {

    val allSongs: SharedFlow<List<LocalSong>>

    /**
     * Reads media store, should be called only once during initialization
     */
    suspend fun initializeSongList()

    /**
     * Rereads media store
     */
    fun reset()
}
