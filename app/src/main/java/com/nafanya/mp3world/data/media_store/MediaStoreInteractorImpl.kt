package com.nafanya.mp3world.data.media_store

import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Class that reads local MediaStore.
 * @property allSongs holds all song objects that device has
 *
 * note: it's going to be tricky to make it initialize by itself since we need the storage access permission
 */
class MediaStoreInteractorImpl @Inject constructor(
    private val ioCoroutineProvider: IOCoroutineProvider,
    private val mediaStoreReader: MediaStoreReader
) : MediaStoreInteractor {

    private var isInitialized = AtomicBoolean(false)
    private val mutex = Mutex()

    private val _allSongs = MutableSharedFlow<List<LocalSong>>()
    override val allSongs: SharedFlow<List<LocalSong>>
        get() = _allSongs
            .map {
                it.sortedByDescending { song -> song.date }
            }
            .shareIn(ioCoroutineProvider.ioScope, SharingStarted.Lazily, replay = 1)

    /**
     * Sets managers data on main thread.
     */
    override suspend fun initializeSongList() = mutex.withLock {
        withContext(Dispatchers.IO) {
            if (!isInitialized.getAndSet(true)) {
                val songs = mediaStoreReader.readMediaStore()
                _allSongs.emit(songs)
            }
        }
    }

    /**
     * Resets SongListManager and other managers data on background thread.
     */
    override fun reset() {
        ioCoroutineProvider.ioScope.launch {
            _allSongs.emit(mediaStoreReader.readMediaStore())
        }
    }
}
