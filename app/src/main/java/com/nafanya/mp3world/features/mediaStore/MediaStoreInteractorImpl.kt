package com.nafanya.mp3world.features.mediaStore

import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

/**
 * Class that reads local MediaStore.
 * @property allSongs holds all song objects that device has
 */
@Singleton
class MediaStoreInteractorImpl @Inject constructor(
    private val ioCoroutineProvider: IOCoroutineProvider,
    private val mediaStoreReader: MediaStoreReader
) : MediaStoreInteractor {

    companion object {
        private var isInitialized = false
    }

    private val mAllSongs = MutableSharedFlow<List<LocalSong>>()
    override val allSongs: SharedFlow<List<LocalSong>>
        get() = mAllSongs
            .map {
                it.sortedByDescending { song -> song.date }
            }
            .shareIn(
                ioCoroutineProvider.ioScope,
                replay = 1,
                started = SharingStarted.Lazily
            )

    /**
     * Sets managers data on main thread.
     */
    override fun readMediaStore() {
        if (!isInitialized) {
            ioCoroutineProvider.ioScope.launch {
                isInitialized = true
                mAllSongs.emit(mediaStoreReader.readMediaStore())
            }
        }
    }

    /**
     * Resets SongListManager and other managers data on background thread.
     */
    override fun reset() {
        ioCoroutineProvider.ioScope.launch {
            mAllSongs.emit(mediaStoreReader.readMediaStore())
        }
    }
}
