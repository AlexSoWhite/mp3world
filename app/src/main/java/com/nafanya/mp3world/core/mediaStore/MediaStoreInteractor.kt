package com.nafanya.mp3world.core.mediaStore

import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * Class that reads local MediaStore.
 * @property allSongs holds all song objects that device has
 */
@Singleton
class MediaStoreInteractor @Inject constructor(
    private val ioCoroutineProvider: IOCoroutineProvider,
    private val mediaStoreReader: MediaStoreReader
) {

    companion object {
        private var isInitialized = false
    }

    val allSongs: SharedFlow<List<LocalSong>?>
        get() = mediaStoreReader.songList

    /**
     * Sets managers data on main thread.
     */
    fun readMediaStore() {
        if (!isInitialized) {
            ioCoroutineProvider.ioScope.launch {
                mediaStoreReader.readMediaStore()
                isInitialized = true
            }
        }
    }

    /**
     * Resets SongListManager and other managers data on background thread.
     */
    fun reset() {
        ioCoroutineProvider.ioScope.launch {
            mediaStoreReader.readMediaStore()
        }
    }
}
