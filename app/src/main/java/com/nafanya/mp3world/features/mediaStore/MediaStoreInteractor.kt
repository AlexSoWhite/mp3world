package com.nafanya.mp3world.features.mediaStore

import com.nafanya.mp3world.core.wrappers.LocalSong
import kotlinx.coroutines.flow.SharedFlow

interface MediaStoreInteractor {

    val allSongs: SharedFlow<List<LocalSong>>

    /**
     * Reads media store, should be called only once during initialization
     */
    fun readMediaStore()

    /**
     * Rereads media store
     */
    fun reset()
}
