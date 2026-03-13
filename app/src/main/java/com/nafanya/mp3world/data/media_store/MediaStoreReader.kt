package com.nafanya.mp3world.data.media_store

import com.nafanya.mp3world.core.wrappers.song.local.LocalSong

interface MediaStoreReader {

    suspend fun readMediaStore(): List<LocalSong>
}
