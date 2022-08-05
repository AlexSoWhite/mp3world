package com.nafanya.mp3world.core.listManagers

import com.nafanya.mp3world.core.mediaStore.MediaStoreReader

interface ListManager {

    suspend fun populate(mediaStoreReader: MediaStoreReader)
}
