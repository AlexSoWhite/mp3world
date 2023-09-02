package com.nafanya.mp3world.features.mediaStore

import androidx.annotation.WorkerThread
import com.nafanya.mp3world.core.wrappers.LocalSong

interface MediaStoreReader {

    @WorkerThread
    fun readMediaStore(): List<LocalSong>
}
