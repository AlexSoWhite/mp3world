package com.nafanya.mp3world.core.listManagers

import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import javax.inject.Inject

class ListManagerContainer @Inject constructor(
    private val listManagers: Set<@JvmSuppressWildcards ListManager>
) {

    suspend fun populateAll(mediaStoreReader: MediaStoreReader) {
        listManagers.forEach {
            it.populate(mediaStoreReader)
        }
    }
}
